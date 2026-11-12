import GaleWingApi from '../../api/galeWingApi';
import SettingApi from '../../api/settingApi';
import 'ag-grid-community/styles/ag-grid.css';
import 'ag-grid-community/styles/ag-theme-alpine.css';

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
  IRowNode,
  ModuleRegistry,
  createGrid,
} from 'ag-grid-community';

// Community版の全機能を登録
ModuleRegistry.registerModules([AllCommunityModule]);

export default class GaleWingGrid {
  private static singleton: GaleWingGrid;

  private constructor() { }

  private stopRowClickFlg: boolean = false;

  public gridApi: GridApi = null;

  static getInstance() {
    if (!this.singleton) {
      this.singleton = new GaleWingGrid();
      this.singleton.setupGrid();
    }

    return this.singleton;
  }

  private async setupGrid() {
    const api = GaleWingApi.getInstance();
    const res = await api.getFeedList();

    const setting = new SettingApi();
    await setting.init();
    setting.outputLog();
    const limit = Number(setting.get('feed_rows'));

    console.log('GaleWingGrid setupGrid res.data:', res.data);

    const gridOptions: GridOptions = {
      pagination: true,
      paginationPageSize: limit,
      suppressPaginationPanel: true,
      domLayout: "autoHeight",
      rowData: res.data,
      rowHeight: 40,
      columnDefs: [
        {
          field: "title", hide: false, cellRenderer: this.titleCellRenderer.bind(this),
          flex: 1,
          wrapText: true,
          autoHeight: true,
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
        if (event.colDef.field !== 'title') return;

        const rowNode = event.node;
        rowNode.setDataValue('readed', true);

        this.readRow(event.data.link, rowNode);
      },
      onGridReady: () => this.updatePaginationUI(),
      onPaginationChanged: () => this.updatePaginationUI(),
    };
    const wrapper = document.getElementById('wrapper');
    if (!wrapper) throw new Error('Wrapper element not found');

    this.gridApi = createGrid(wrapper, gridOptions);

    // 各種ボタンの初期化
    this.setupGridEvents();
  }

  private titleCellRenderer(params: ICellRendererParams<RowData>) {
    return this.createTitleLinkText(
      params.value,
      params.data.link,
      params.data.readed,
    );
  }

  private setupGridEvents() {

    this.setupGridHeaderEvents();
    this.setupGridFooterEvents();

  }

  private setupGridHeaderEvents() {

    this.setupGridHeaderButtonEvents();

    document.getElementById('header-pageSize')?.addEventListener('change', (e) => {
      const target = e.target as HTMLSelectElement;
      if (target && this.gridApi) {
        const newSize = Number(target.value);
        // v31以降の推奨APIでページサイズを変更
        this.gridApi.setGridOption('paginationPageSize', newSize);
      }
    });

    document.getElementById('header-nowPage')?.addEventListener('change', () => {
      this.paginationToggleButtonState('header-nowPage');
    });

    document.getElementById('header-nowPage')?.addEventListener('keydown', (e) => {
      if (e.key === 'Enter') {
        this.paginationToggleButtonState('header-nowPage');
      }
    });
  }

  setupGridHeaderButtonEvents() {
    // 各種ボタンの初期化
    const headerFirstBtn = document.getElementById('header-firstBtn');
    headerFirstBtn.addEventListener('click', () => {
      this.gridApi?.paginationGoToFirstPage();
    });

    const headerPrevBtn = document.getElementById('header-prevBtn');
    headerPrevBtn.addEventListener('click', () => {
      this.readDispRow();
      this.gridApi?.paginationGoToPreviousPage();
    });

    const headerNextBtn = document.getElementById('header-nextBtn');
    headerNextBtn.addEventListener('click', () => {
      this.readDispRow();
      this.gridApi?.paginationGoToNextPage();
    });

    const headerLastBtn = document.getElementById('header-lastBtn');
    headerLastBtn.addEventListener('click', () => {
      this.gridApi?.paginationGoToLastPage();
    });
  }

  private setupGridFooterEvents() {
    this.setupGridFooterButtonEvents();

    document.getElementById('footer-pageSize')?.addEventListener('change', (e) => {
      const target = e.target as HTMLSelectElement;
      if (target && this.gridApi) {
        const newSize = Number(target.value);
        this.gridApi.setGridOption('paginationPageSize', newSize);
      }
    });

    document.getElementById('footer-nowPage')?.addEventListener('change', () => {
      this.paginationToggleButtonState('footer-nowPage');
    });

    document.getElementById('footer-nowPage')?.addEventListener('keydown', (e) => {
      if (e.key !== 'Enter') return;
      this.paginationToggleButtonState('footer-nowPage');
    });
  }

  private paginationToggleButtonState(pageElId: string) {
    const pageEl = document.getElementById(pageElId)! as HTMLInputElement;
    const newPage = Number(pageEl.value) - 1; // ページ番号は0始まり
    this.gridApi.paginationGoToPage(newPage);
  }

  private setupGridFooterButtonEvents() {
    const firstBtn = document.getElementById('footer-firstBtn');
    firstBtn.addEventListener('click', () => {
      this.gridApi?.paginationGoToFirstPage();
    });

    const prevBtn = document.getElementById('footer-prevBtn');
    prevBtn.addEventListener('click', () => {
      this.readDispRow();
      this.gridApi?.paginationGoToPreviousPage();
    });

    const nextBtn = document.getElementById('footer-nextBtn');
    nextBtn.addEventListener('click', () => {
      this.readDispRow();
      this.gridApi?.paginationGoToNextPage();
    });

    const lastBtn = document.getElementById('footer-lastBtn');
    lastBtn.addEventListener('click', () => {
      this.gridApi?.paginationGoToLastPage();
    });
  }

  private createTitleLinkText(
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

  /**
 * ページネーション状態やUI（ボタンの有効・無効、ページ数表示）を更新する関数
 */
  updatePaginationUI(): void {
    if (!this.gridApi) return;

    // ページ番号は0始まりのため、表示用に +1 する
    const currentPage = this.gridApi.paginationGetCurrentPage() + 1;
    const totalPages = this.gridApi.paginationGetTotalPages();

    this.updateHeaderPaginationUI(currentPage, totalPages);
    this.updateFooterPaginationUI(currentPage, totalPages);
  }

  /**
   * ヘッダーのページネーションUIを更新する
   * @param currentPage 表示中のページ
   * @param totalPages 最大ページ
   */
  private updateHeaderPaginationUI(currentPage: number, totalPages: number): void {
    const headerPageInfoEl = document.getElementById('header-pageInfo');
    const headerNowPageEl = document.getElementById('header-nowPage') as HTMLInputElement | null;
    const headerViewDataInfoEl = document.getElementById('header-viewDataInfo') as HTMLDivElement | null;
    const headerPageSizeEl = document.getElementById('header-pageSize') as HTMLInputElement | null;
    if (headerPageInfoEl) {
      headerPageInfoEl.innerText = ` / ${totalPages} ページ`;
    }

    if (headerNowPageEl) headerNowPageEl.value = currentPage.toString();
    if (headerPageSizeEl) headerPageSizeEl.value = this.gridApi.getGridOption('paginationPageSize').toString();
    if (headerViewDataInfoEl) headerViewDataInfoEl.innerText = this.createViewDataInfoText(currentPage);
  }

  /**
   * フッターのページネーションUIを更新する
   * @param currentPage 表示中のページ
   * @param totalPages 最大ページ
   */
  private updateFooterPaginationUI(currentPage: number, totalPages: number): void {
    const footerPageInfoEl = document.getElementById('footer-pageInfo');
    const footerNowPageEl = document.getElementById('footer-nowPage') as HTMLInputElement | null;
    const footerViewDataInfoEl = document.getElementById('footer-viewDataInfo') as HTMLDivElement | null;
    const footerPageSizeEl = document.getElementById('footer-pageSize') as HTMLInputElement | null;

    if (footerPageInfoEl) {
      footerPageInfoEl.innerText = ` / ${totalPages} ページ`;
    }

    if (footerNowPageEl) footerNowPageEl.value = currentPage.toString();
    if (footerPageSizeEl) footerPageSizeEl.value = this.gridApi.getGridOption('paginationPageSize').toString();
    if (footerViewDataInfoEl) {
      footerViewDataInfoEl.innerText = this.createViewDataInfoText(currentPage);
    }
  }

  private createViewDataInfoText(currentPage: number): string {
    let count = 0;
    this.gridApi.forEachNode(() => count++);
    const start = (currentPage - 1) * this.gridApi.getGridOption('paginationPageSize') + 1;
    const end = Math.min(currentPage * this.gridApi.getGridOption('paginationPageSize'), this.gridApi.paginationGetTotalPages());
    return `${start} to ${end} of ${count}`;
  }

  private readDispRow() {
    this.gridApi.getRenderedNodes()
      .forEach((node) => {
        const link = node.data.link;
        node.setDataValue('readed', true);
        this.readRow(link, node);
      });
  }

  private readRow(link: string, node: IRowNode) {
    GaleWingApi.getInstance().read(link)
      .then(() => {
        GaleWingGrid.getInstance().gridApi.refreshCells({
          rowNodes: [node],
          force: true,
          columns: ['title'],
        });
      })
      .catch((error) => {
        console.error('GaleWingGrid onCellClicked read error:', error);
      });
  }
}
