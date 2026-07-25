import GaleWingApi from '../../api/galeWingApi';
import { Row, html, h } from 'gridjs';
import * as gridjs from "gridjs";
console.log(gridjs);
import { VNode } from 'preact';
import ReadAllShowFeed from '../../events/readAllShowFeed';
import UpdateFeed from '../../events/updateFeed';
import init from '../cardGridLayout';
import SettingApi from '../../api/settingApi';
import ElementEvent from '../../events/elementEvent';
import CirculationEvent from '../../events/circulationEvent';
import SummaryEvent from '../../events/summaryEvent';
import InformationGatheringEvent from '../../events/informationGatheringEvent';

export enum HeaderIndex {
  title,
  link,
  uri,
  type,
  author,
  comments,
  publishedDate,
  uuid,
  imageUrl,
  chkSts,
};

export default class GaleWingGrid {
  private static singleton: GaleWingGrid;

  private constructor() { }

  private stopRowClickFlg: boolean = false;

  public data: any = [];

  public grid: gridjs.Grid | null = null;

  static getInstance() {
    if (!this.singleton) {
      this.singleton = new GaleWingGrid();
      this.singleton.setupGrid();
    }

    return this.singleton;
  }

  private setupGrid() {
    const api = GaleWingApi.getInstance();
    api
      .getFeedList(window.location.href)
      .then(async (res) => {
        this.data = res.data;

        const setting = new SettingApi();
        await setting.init();
        setting.outputLog();
        const limit = Number(setting.get('feed_rows'));

        await this.createGrid(res.data, limit);

        this.setupGridEvent(this.grid, limit);

        init(9, 3, res.data);
      })
      .catch((error) => {
        throw new Error(error);
      });
  }

  async createGrid(data: any, limit: any) {

    this.grid = new gridjs.Grid({
      columns: this.createGridColumn(),
      pagination: {
        limit: Number(limit),
      },
      sort: true,
      search: false,
      data: data,
    });

    this.grid.render(<HTMLInputElement>document.getElementById('wrapper'));
  }

  createGridColumn() {
    return [
      {
        name: 'title',
        hidden: false,
        formatter: (
          cell: any,
          row: {
            cells: {
              data: string | number | bigint | boolean | object | VNode<any> | null | undefined;
            }[];
          },
        ) =>
          html(
            this.createTitleLinkText(
              row.cells[HeaderIndex.title].data,
              row.cells[HeaderIndex.link].data,
              row.cells[HeaderIndex.imageUrl].data,
              row.cells[HeaderIndex.chkSts].data,
            ),
          ),
      },
      { name: 'link', hidden: true },
      { name: 'uri', hidden: true },
      { name: 'type', hidden: true },
      { name: 'author', hidden: true },
      { name: 'comments', hidden: true },
      { name: 'publishedDate', hidden: false },
      { name: 'uuid', hidden: true },
      { name: 'imageUrl', hidden: true },
      { name: 'chkSts', hidden: true },
      {
        name: 'action', hidden: false, formatter: (cell: any, row: Row) => {

          return h('div', { className: 'button-container' }, [
            // 編集ボタン
            h('button', {
              className: 'btn btn-light btn-outline-secondary my-1',
              onClick: () => new SummaryEvent().execute(row.cells[HeaderIndex.link].data!.toString())
            }, '要約'),

            // 削除ボタン
            h('button', {
              className: 'btn btn-light btn-outline-secondary my-1',
              onClick: () => new InformationGatheringEvent().execute(row.cells[HeaderIndex.link].data!.toString())
            }, '収集')
          ]);

          // return h('button', {
          //   className: 'btn btn-light btn-outline-secondary',
          //   onClick: () => {
          //     new relationListEvent().execute(row.cells[HeaderIndex.link].data!.toString());
          //   }
          // }, 'rel');
        }, attributes: { style: 'padding: 1px' }
      }
    ];
  }

  createTitleLinkText(
    title: string | number | bigint | boolean | object | VNode<any> | null | undefined,
    link: string | number | bigint | boolean | object | VNode<any> | null | undefined,
    imageUrl: string | number | bigint | boolean | object | VNode<any> | null | undefined,
    chkSts: string | number | bigint | boolean | object | VNode<any> | null | undefined,
  ) {
    return (
      "<div style='display: flex;'>" +
      (imageUrl ? `<img src='${imageUrl}' style='object-fit: cover; height: 80px; width: 80px;'></img>` : '') +
      `<a href='${link}' target="_blank" rel="noopener" class="${chkSts ? 'rss-read-link' : 'rss-link'
      }" data-origin-txt="${title}" data-translation-jp-txt="">${title}</a>` +
      '</div>'
    );
  }

  setupGridEvent(grid: gridjs.Grid, limit: number) {
    new ElementEvent(new UpdateFeed('identifier', grid)).setup(
      'click',
      document.getElementById('updateFeed'),
    );

    new ElementEvent(new ReadAllShowFeed(grid)).setup(
      'click',
      document.getElementById('readAllShowFeed'),
    );

    new ElementEvent(new CirculationEvent(grid, limit)).setup(
      "click",
      document.getElementById("circulation")
    )

    this.setupGridRowClickEvent(grid);
  }

  setupGridRowClickEvent(grid: gridjs.Grid) {

    grid.on('cellClick', (event: Event, ...columns: any[]) => {
      const col = columns[0];
      const colConfig = columns[1];
      const row = columns[2];

      if (colConfig.name === 'title') {
        const link = row.cells[HeaderIndex.link].data!.toLocaleString();

        // リンク押下の場合はリンクの遷移を実施。それ以外は個別にリンクを表示
        if ((event.target as any).localName !== 'a') {
          window.open(link, '_blank');
        }

        const api = GaleWingApi.getInstance();
        api
          .read(link)
          .then(() => {

            // リンク押下の場合はリンクの遷移を実施。それ以外は個別にリンクを表示
            const targetElement: HTMLElement | null = event.target as HTMLElement;
            if (targetElement.tagName.toLowerCase() === 'a') {
              targetElement.classList = 'rss-read-link';
            } else {
              targetElement.getElementsByTagName('a')[0].classList = 'rss-read-link';
            }

            row.cells[HeaderIndex.chkSts].data = '1';
          }).catch((error) => {
            console.error(error);
          });
      }
    });
  }

  stack(uuid: string | null | undefined, link: string | undefined) {
    const api = GaleWingApi.getInstance();
    api.stackFeed(window.location.href, uuid, link);
  }

  setStopRowClickFlg(flg: boolean): void {
    this.stopRowClickFlg = flg;
  }
}
