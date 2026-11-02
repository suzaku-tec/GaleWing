import GaleWingApi from '../../api/galeWingApi';
import SettingApi from '../../api/settingApi';

interface RowData {
  title: string;
  link: string;
  uri: string;
  type: string;
  author: string;
  comments: string;
  publishedDate: string;
  uuid: string;
  imageUrl: string;
  readed: boolean;
}

import {
  AllCommunityModule,
  CellClickedEvent,
  GridApi,
  GridOptions,
  ICellRendererParams,
  ModuleRegistry,
  createGrid,
} from 'ag-grid-community';

// Community版の全機能を登録
ModuleRegistry.registerModules([AllCommunityModule]);

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

  public gridApi: GridApi<any> = null;

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
      .getFeedList()
      .then(async (res) => {
        this.data = res.data;

        const setting = new SettingApi();
        await setting.init();
        setting.outputLog();
        const limit = Number(setting.get('feed_rows'));

        console.log('GaleWingGrid setupGrid res.data:', res.data);

        const gridOptions: GridOptions = {
          pagination: true,
          paginationPageSize: limit,
          domLayout: "autoHeight",
          rowData: res.data,
          columnDefs: [
            {
              field: "title", hide: false, cellRenderer: (params: ICellRendererParams<RowData>) => {
                console.log('GaleWingGrid cellRenderer params:', params);
                return this.createTitleLinkText(
                  params.value,
                  params.data.link,
                  params.data.readed,
                );
              },
              flex: 1,
            },
            { field: 'link', hide: true },
            { field: 'uri', hide: true },
            { field: 'type', hide: true },
            { field: 'author', hide: true },
            { field: 'comments', hide: true },
            { field: 'publishedDate', hide: false },
            { field: 'uuid', hide: true },
            { field: 'imageUrl', hide: true },
            { field: 'readed', hide: true },],
          onCellClicked: (event: CellClickedEvent) => {
            if (event.colDef.field !== 'title') {
              return;
            }

            const rowNode = event.node;
            const currentValue = rowNode.getDataValue('readed');

            rowNode.setDataValue('readed', currentValue ? false : true);

            GaleWingApi.getInstance().read(event.data.link)
              .then(() => {
                console.log('GaleWingGrid onCellClicked read success:', event.data.link);
                console.log('GaleWingGrid onCellClicked event:', event);
                event.api.refreshCells({
                  rowNodes: [rowNode],
                  columns: ['title'],
                  force: true,
                });
              })
              .catch((error) => {
                console.error('GaleWingGrid onCellClicked read error:', error);
              });

          },
        };
        const wrapper = document.getElementById('wrapper');
        if (!wrapper) {
          throw new Error('Wrapper element not found');
        }
        this.gridApi = createGrid(wrapper, gridOptions);
      })
      .catch((error) => {
        throw new Error(error);
      });
  }

  createTitleLinkText(
    title: string,
    link: string,
    chkSts: boolean,
  ) {

    const anchor = document.createElement("a");
    anchor.href = link;
    anchor.target = "_blank";
    anchor.rel = "noopener";
    anchor.className = chkSts ? "rss-read-link" : "rss-link";
    anchor.dataset.originTxt = title;
    anchor.dataset.translationJpTxt = "";
    anchor.textContent = title;

    const div = document.createElement("div");
    div.style.display = "flex";
    div.appendChild(anchor);

    return div;
  }

  stack(uuid: string | null | undefined, link: string | undefined) {
    const api = GaleWingApi.getInstance();
    api.stackFeed(window.location.href, uuid, link);
  }

  setStopRowClickFlg(flg: boolean): void {
    this.stopRowClickFlg = flg;
  }

}
