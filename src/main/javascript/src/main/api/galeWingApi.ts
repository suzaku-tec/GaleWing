import axios, { AxiosResponse } from 'axios';

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
  };

  private static singleton: GaleWingApi;

  private constructor() {}

  getFeedList(url: string): Promise<AxiosResponse<any>> {
    var baseUrl = new URL(url);
    var ajaxUrl = baseUrl.origin + this.apiUrls.feedList + baseUrl.search;
    return axios.get(ajaxUrl);
  }

  getSiteList(url: string): Promise<AxiosResponse<any>> {
    var baseUrl = new URL(url);
    var ajaxUrl = baseUrl.origin + this.apiUrls.siteList;
    return axios.get(ajaxUrl);
  }

  deleteSite(location: string, uuid: string) {
    var baseUrl = new URL(location);
    var ajaxUrl = baseUrl.origin + this.apiUrls.deleteSite;
    return axios.post(ajaxUrl, { uuid: uuid });
  }

  getStackList(url: string): Promise<AxiosResponse<any>> {
    var baseUrl = new URL(url);
    var ajaxUrl = baseUrl.origin + this.apiUrls.stackList;
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
    var baseUrl = new URL(url);
    var ajaxUrl = baseUrl.origin + this.apiUrls.stackFeed;
    return axios.post(ajaxUrl, { uuid: uuid, link: link });
  }

  settingJson(): Promise<AxiosResponse<any>> {
    var baseUrl = new URL(window.location.href);
    var ajaxUrl = baseUrl.origin + this.apiUrls.settingJson;
    return axios.get(ajaxUrl);
  }

  jaroWinklerDistance(title: string): Promise<AxiosResponse<any>> {
    var baseUrl = new URL(window.location.href);
    var ajaxUrl = baseUrl.origin + this.apiUrls.jaroWinklerDistance;
    return axios.get(ajaxUrl, { params: { targetTitle: title } });
  }

  analysisFeedAllRead(links: string[]): Promise<AxiosResponse<any>> {
    var baseUrl = new URL(window.location.href);
    var ajaxUrl = baseUrl.origin + this.apiUrls.analysisFeedAllRead;
    return axios.post(ajaxUrl, { links: links });
  }

  siteCategoryList(siteUuid: string): Promise<AxiosResponse<any>> {
    let baseUrl = new URL(window.location.href);
    let ajaxUrl = baseUrl.origin + this.apiUrls.siteCategoryList;
    return axios.post(ajaxUrl, { siteUuid: siteUuid });
  }

  addCategory(name: string, description: string): Promise<AxiosResponse<any>> {
    let baseUrl = new URL(window.location.href);
    let ajaxUrl = baseUrl.origin + this.apiUrls.addCategory;
    return axios.post(ajaxUrl, { name: name, description: description });
  }

  deleteCategory(uuid: string): Promise<AxiosResponse<any>> {
    let baseUrl = new URL(window.location.href);
    let ajaxUrl = baseUrl.origin + this.apiUrls.deleteCategory;
    return axios.post(ajaxUrl, { uuid: uuid });
  }

  addSiteCategory(siteUuid: string, categoryUuid: string): Promise<AxiosResponse<any>> {
    let baseUrl = new URL(window.location.href);
    let ajaxUrl = baseUrl.origin + this.apiUrls.addSiteCategory;
    return axios.post(ajaxUrl, { siteUuid: siteUuid, categoryUuid: categoryUuid });
  }

  deleteSiteCategory(siteUuid: string, categoryUuid: string): Promise<AxiosResponse<any>> {
    let baseUrl = new URL(window.location.href);
    let ajaxUrl = baseUrl.origin + this.apiUrls.deleteSiteCategory;
    return axios.post(ajaxUrl, { siteUuid: siteUuid, categoryUuid: categoryUuid });
  }

  static getInstance() {
    if (!this.singleton) {
      this.singleton = new GaleWingApi();
    }

    return this.singleton;
  }
}
