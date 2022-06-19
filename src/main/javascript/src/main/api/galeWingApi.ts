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

  static getInstance() {
    if (!this.singleton) {
      this.singleton = new GaleWingApi();
    }

    return this.singleton;
  }
}
