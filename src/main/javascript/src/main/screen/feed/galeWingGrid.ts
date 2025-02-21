import GaleWingApi from '../../api/galeWingApi';
import { Grid, Row, html } from 'gridjs';
import { JSX, VNode } from 'preact';
import ReadAllShowFeed from '../../events/readAllShowFeed';
import UpdateFeed from '../../events/updateFeed';
import init from '../cardGridLayout';
import SettingApi from '../../api/settingApi';
import ElementEvent from '../../events/elementEvent';
import CirculationEvent from '../../events/circulationEvent';
import SummaryEvent from '../../events/summaryEvent';

enum headerIndexConfig {
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

  public grid: Grid | null = null;

  static getInstance() {
    if (!this.singleton) {
      this.singleton = new GaleWingGrid();
      this.singleton.setupGrid();
    }

    return this.singleton;
  }

  private setupGrid() {
    let api = GaleWingApi.getInstance();
    api
      .getFeedList(window.location.href)
      .then(async (res) => {
        this.data = res.data;

        let setting = new SettingApi();
        await setting.init();
        setting.outputLog();
        var limit = Number(setting.get('feed_rows'));

        this.grid = await this.createGrid(res.data, limit);

        this.setupGridEvent(this.grid, limit);

        init(9, 3, res.data);
      })
      .catch((error) => {
        throw new Error(error);
      });
  }

  async createGrid(data: any, limit: any) {

    return new Grid({
      columns: this.createGridColumn(),
      pagination: {
        limit: Number(limit),
      },
      sort: true,
      search: true,
      data: data,
    }).render(<HTMLInputElement>document.getElementById('wrapper'));
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
              row.cells[headerIndexConfig.title].data,
              row.cells[headerIndexConfig.link].data,
              row.cells[headerIndexConfig.imageUrl].data,
              row.cells[headerIndexConfig.chkSts].data,
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
      (imageUrl ? `<img src='${imageUrl}' style='object-fit: contain; height: 100px'></img>` : '') +
      `<a href='${link}' target="_blank" rel="noopener" class="${chkSts ? 'rss-read-link' : 'rss-link'
      }" data-origin-txt="${title}" data-translation-jp-txt="">${title}</a>` +
      '</div>'
    );
  }

  setupGridEvent(grid: Grid, limit: Number) {
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

    new ElementEvent(new SummaryEvent(grid, limit)).setup(
      "click",
      document.getElementById("summary")
    )

    this.setupGridRowClickEvent(grid);
  }

  setupGridRowClickEvent(grid: Grid) {
    grid.on('rowClick', (event, row) => {
      var tmp = event as any;
      if (tmp.originalTarget.type == "checkbox") {
        return
      }


      let link: string | undefined = undefined;
      if ((event.target as any).localName == 'path') {
        let uuid = row?.cell(headerIndexConfig.uuid).data?.toLocaleString();
        link = row?.cell(headerIndexConfig.link).data?.toLocaleString();
        this.stack(uuid, link);
        return;
      }

      if (
        (event.target as any).name === 'analysis' ||
        ((event.target as Element).parentElement as HTMLInputElement).name === 'analysis'
      ) {
        return;
      }

      link = row?.cell(headerIndexConfig.link).data?.toLocaleString();
      if (!this.stopRowClickFlg) {
        if ((event.target as any).localName != 'a') {
          window.open(link);
        }
      }

      if (!link) {
        return;
      }

      this.changeRowRead(link, event, row);
    });
  }

  changeRowRead(link: string, event: JSX.TargetedMouseEvent<HTMLTableRowElement>, row: Row) {
    let api = GaleWingApi.getInstance();
    api
      .read(link)
      .then(() => {
        // 既読表示に変更
        if ((event.target as any).localName == 'a') {
          (event.target as HTMLElement).classList.remove('rss-link');
          (event.target as HTMLElement).classList.add('rss-read-link');
        } else {
          let innerHTML = (event.target as HTMLElement).innerHTML;
          (event.target as any).innerHTML = innerHTML.replace(/rss-link/g, 'rss-read-link');
        }

        row.cells[10].data = '1';
      })
      .catch((error) => {
        console.log(error);
      });
  }

  stack(uuid: string | null | undefined, link: string | undefined) {
    let api = GaleWingApi.getInstance();
    api.stackFeed(window.location.href, uuid, link);
  }

  setStopRowClickFlg(flg: boolean): void {
    this.stopRowClickFlg = flg;
  }
}
