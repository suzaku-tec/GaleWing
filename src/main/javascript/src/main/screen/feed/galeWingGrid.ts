import GaleWingApi from '../../api/galeWingApi';
import SettingApi from '../../api/settingApi';
import 'ag-grid-community/styles/ag-grid.css';
import 'ag-grid-community/styles/ag-theme-alpine.css';
import {
  AllCommunityModule,
  CellClickedEvent,
  createGrid,
  GridApi,
  GridOptions,
  ICellRendererParams,
  IRowNode,
  ModuleRegistry,
} from 'ag-grid-community';
import FeedApi from '../../api/disp/feedApi';

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

// Community版の全機能を登録
ModuleRegistry.registerModules([AllCommunityModule]);

export default class GaleWingGrid {
  private static singleton: GaleWingGrid;

  private static relatedLinkOpenFlg: boolean = false;

  private constructor() {
  }

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
    const functionCtrlList = await api.functionCtrl();
    functionCtrlList.data.forEach((ctrl) => {
      if (ctrl.id === 'relatedLink') {
        GaleWingGrid.relatedLinkOpenFlg = ctrl.flg === '1';
      }
    });
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

        this.readRow(event.data.link, rowNode, true, GaleWingGrid.relatedLinkOpenFlg);
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

  private async readDispRow(): Promise<void> {
    const nodes = this.gridApi.getRenderedNodes();

    await Promise.allSettled(
      nodes.map(async (node) => {
        const link = node.data.link;
        node.setDataValue("readed", true);
        await this.readRow(link, node, false);
      })
    );

    await this.updateSiteUnreadCount();
  }

  /**
   * 対象行を既読にする
   *
   * @param link 対象リンク
   * @param node 行情報
   * @param isUpdateUnreadCount 未読数更新フラグ true:更新 false:更新しない
   * @param isRelatedLinkOpen 関連リンクを開くフラグ true:開く false:開かない
   */
  private async readRow(link: string, node: IRowNode, isUpdateUnreadCount: boolean = true, isRelatedLinkOpen: boolean = false): Promise<void> {
    const galeWingApi = GaleWingApi.getInstance();
    await galeWingApi.read(link);
    if (isUpdateUnreadCount) {
      await this.updateSiteUnreadCount();
    }
    GaleWingGrid.getInstance().gridApi.refreshCells({
      rowNodes: [node],
      force: true,
      columns: ['title'],
    });

    if (isRelatedLinkOpen) {
      const res = await galeWingApi.relatedLinks(link, 0.4);
      await Promise.allSettled(
        res.data.map(async (relatedLink: { link: string }) => {
          await this.readRelatedLink(relatedLink.link);
        })
      );
    }
  }

  /**
   * 関連リンクを既読にする
   * 
   * @param link 対象リンク
   */
  private async readRelatedLink(link: string): Promise<void> {
    console.log(`Reading related link: ${link}`);
    window.open(link, "_blank", "noopener,noreferrer");

    const galeWingApi = GaleWingApi.getInstance();
    await galeWingApi.read(link);

    this.gridApi.forEachNode((rowNode) => {
      if (rowNode.data.link === link) {
        rowNode.setDataValue("readed", true);
        GaleWingGrid.getInstance().gridApi.refreshCells({
          rowNodes: [rowNode],
          force: true,
          columns: ['title'],
        });
      }
    });

    await this.updateSiteUnreadCount();
  }

  /**
   * サイトの未読数を更新する
   */
  private async updateSiteUnreadCount(): Promise<void> {
    const uuid = new URL(window.location.href).searchParams.get("uuid");
    const res = (await GaleWingApi.getInstance().countSiteUnread(uuid)).data;
    res.forEach((countInfo) => {
      if (!countInfo || !countInfo.uuid || countInfo.count === 0) {
        console.info("No countInfo or uuid or count is zero, skipping update.");
        return;
      }
      FeedApi.getInstance().unreadUpdate(countInfo.uuid, countInfo.count.toString());
    });
  }
}
