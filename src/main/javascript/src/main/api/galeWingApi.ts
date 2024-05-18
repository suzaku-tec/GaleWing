import axios, { AxiosResponse } from 'axios';
import FeedApi from './disp/feedApi';

export default class GaleWingApi {
  public readonly apiUrls = {
    feedList: '/feedlist',
    siteList: '/sitelist',
    deleteSite: '/site/delete',
    stackList: '/stack/list',
    stackFeed: '/stack/add',
    settingJson: '/settings/list/json',
    jaroWinklerDistance: '/analysis/jaroWinklerDistance',
    analysisFeedAllRead: '/analysis/feed/allRead',
    siteCategoryList: '/category/site/list',
    deleteCategory: '/category/delete',
    addCategory: '/category/add',
    addSiteCategory: '/siteCategory/add',
    deleteSiteCategory: '/siteCategory/delete',
    translationEnJp: '/minhon/transelate',
    read: '/read',
    executeStatsSql: '/stats/executeStatsSql',
    statsIdList: '/stats/statsIdList',
    executeTask: '/task/executeTask',
    circulationAdd: "/circulation/add",
    circulationList: "/circulation/list",
    circulationStatusList: "/circulation/status/list",
    updateIcon: "/site/updateIcon",
    summaryDelete: "/news/summary/delete",
    summaryAdd: "/news/summary/add",
    functionCtrlUpdate: "/functionCtrl/update",
    viewsSave: '/views/save'
  };

  private static singleton: GaleWingApi;

  private constructor() {}

  getFeedList(url: string): Promise<AxiosResponse<any>> {
    let baseUrl = new URL(window.location.href);
    this.checkUrl(baseUrl);
    let ajaxUrl = baseUrl.origin + this.apiUrls.feedList + baseUrl.search;
    return axios.get(ajaxUrl);
  }

  getSiteList(url: string): Promise<AxiosResponse<any>> {
    let ajaxUrl = this.getBaseUrl() + this.apiUrls.siteList;
    return axios.get(ajaxUrl);
  }

  deleteSite(location: string, uuid: string) {
    let ajaxUrl = this.getBaseUrl() + this.apiUrls.deleteSite;
    return axios.post(ajaxUrl, { uuid: uuid });
  }

  getStackList(url: string): Promise<AxiosResponse<any>> {
    let baseUrl = new URL(url);
    this.checkUrl(baseUrl);
    let ajaxUrl = baseUrl.origin + this.apiUrls.stackList;
    return axios.get(ajaxUrl);
  }

  stackFeed(
    url: string,
    uuid: string | null | undefined,
    link: string | undefined,
  ): Promise<AxiosResponse<any>> {
    if (!uuid) {
      return Promise.reject();
    }
    let ajaxUrl = this.getBaseUrl() + this.apiUrls.stackFeed;
    return axios.post(ajaxUrl, { uuid: uuid, link: link });
  }

  settingJson(): Promise<AxiosResponse<any>> {
    let ajaxUrl = this.getBaseUrl() + this.apiUrls.settingJson;
    return axios.get(ajaxUrl);
  }

  jaroWinklerDistance(title: string): Promise<AxiosResponse<any>> {
    let ajaxUrl = this.getBaseUrl() + this.apiUrls.jaroWinklerDistance;
    return axios.get(ajaxUrl, { params: { targetTitle: title } });
  }

  analysisFeedAllRead(links: string[]): Promise<AxiosResponse<any>> {
    let ajaxUrl = this.getBaseUrl() + this.apiUrls.analysisFeedAllRead;
    return axios.post(ajaxUrl, { links: links });
  }

  siteCategoryList(siteUuid: string): Promise<AxiosResponse<any>> {
    let ajaxUrl = this.getBaseUrl() + this.apiUrls.siteCategoryList;
    return axios.post(ajaxUrl, { siteUuid: siteUuid });
  }

  addCategory(name: string, description: string): Promise<AxiosResponse<any>> {
    let ajaxUrl = this.getBaseUrl() + this.apiUrls.addCategory;
    return axios.post(ajaxUrl, { name: name, description: description });
  }

  deleteCategory(uuid: string): Promise<AxiosResponse<any>> {
    let ajaxUrl = this.getBaseUrl() + this.apiUrls.deleteCategory;
    return axios.post(ajaxUrl, { uuid: uuid });
  }

  addSiteCategory(siteUuid: string, categoryUuid: string): Promise<AxiosResponse<any>> {
    return this.fixSiteCategory(this.apiUrls.addSiteCategory, siteUuid, categoryUuid);
  }

  deleteSiteCategory(siteUuid: string, categoryUuid: string): Promise<AxiosResponse<any>> {
    return this.fixSiteCategory(this.apiUrls.deleteSiteCategory, siteUuid, categoryUuid);
  }

  executeStatsSql(id: string): Promise<AxiosResponse<any>> {
    let ajaxUrl = this.getBaseUrl() + this.apiUrls.executeStatsSql;
    return axios.post(ajaxUrl, { id: id });
  }

  getStatsIdList(): Promise<AxiosResponse<any>> {
    let ajaxUrl = this.getBaseUrl() + this.apiUrls.statsIdList;
    return axios.post(ajaxUrl);
  }

  updateIcon(uuid: string): Promise<AxiosResponse<any>> {
    let ajaxUrl = this.getBaseUrl() + this.apiUrls.updateIcon;
    return axios.post(ajaxUrl, {uuid: uuid});
  }

  viewSave(viewId: string, viewName: string, siteIdList: string[]): Promise<AxiosResponse<any>> {
    let ajaxUrl = this.getBaseUrl() + this.apiUrls.viewsSave;
    return axios.post(ajaxUrl, {viewId: viewId, viewName: viewName, siteIdList: siteIdList});
  }

  private fixSiteCategory(
    kbn: string,
    siteUuid: string,
    categoryUuid: string,
  ): Promise<AxiosResponse<any>> {
    let url = this.getBaseUrl() + kbn;
    return axios.post(url, { siteUuid: siteUuid, categoryUuid: categoryUuid });
  }

  translationEnJp(text: string): Promise<AxiosResponse<any>> {
    let ajaxUrl = this.getBaseUrl() + this.apiUrls.translationEnJp;
    return axios.post(ajaxUrl, { text: text }, { headers: { 'Content-Type': 'application/json' } });
  }

  async read(link: string) {
    let ajaxUrl = this.getBaseUrl() + this.apiUrls.read;
    const response = await axios.post(ajaxUrl, {
      link: link,
    });
    // 未読数の更新
    response.data.forEach((element: { uuid: string; count: number }) => {
      FeedApi.getInstance().unreadUpdate(element.uuid, element.count.toString());
    });
  }

  async executeTask(taskName: string) {
    let ajaxUrl = this.getBaseUrl() + this.apiUrls.executeTask;
    await axios.post(ajaxUrl, {
      name: taskName,
    });
  }

  async circulationAdd(link: string, title: string) {
    let ajaxUrl = this.getBaseUrl() + this.apiUrls.circulationAdd;
    await axios.post(ajaxUrl, {
      link: link,
      title: title
    });
  }

  async circulationList() {
    let ajaxUrl = this.getBaseUrl() + this.apiUrls.circulationList;
    return await axios.post(ajaxUrl);
  }

  async circulationStatusList() {
    let ajaxUrl = this.getBaseUrl() + this.apiUrls.circulationStatusList;
    return await axios.post(ajaxUrl);
  }

  async deleteSummary(uuid: string) {
    let ajaxUrl = this.getBaseUrl() + this.apiUrls.summaryDelete;
    return await axios.post(ajaxUrl + "?uuid=" + uuid);
  }

  async summaryAdd(uuid: string) {
    let ajaxUrl = this.getBaseUrl() + this.apiUrls.summaryAdd + "?uuid=" + uuid;
    await axios.post(ajaxUrl);
  }

  async functionCtrlUpdate(id: string, flg: string) {
    let ajaxUrl = this.getBaseUrl() + this.apiUrls.functionCtrlUpdate;
    return await axios.post(ajaxUrl, {
      id: id,
      flg: flg
    });
  }

  private getBaseUrl(): string {
    let baseUrl = new URL(window.location.href);
    this.checkUrl(baseUrl);
    return baseUrl.origin;
  }
  private checkUrl(url: URL) {
    if (url.protocol !== 'https:' && url.protocol !== 'http:') {
      throw new GaleWingURLError();
    }
  }

  static getInstance() {
    if (!this.singleton) {
      this.singleton = new GaleWingApi();
    }

    return this.singleton;
  }
}

class GaleWingURLError extends Error {}
