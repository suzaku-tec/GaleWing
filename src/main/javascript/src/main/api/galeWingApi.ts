import axios, { AxiosResponse } from 'axios';

export default class GaleWingApi {
  public readonly apiUrls = {
    feedList: '/feedlist',
    siteList: '/sitelist',
    deleteSite: '/site/delete',
    stackList: '/stack/list',
    stackFeed: '/stack/add',
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

  stackFeed(url: string, uuid: string, link: string): Promise<AxiosResponse<any>> {
    var baseUrl = new URL(url);
    var ajaxUrl = baseUrl.origin + this.apiUrls.stackFeed;
    return axios.post(ajaxUrl, { uuid: uuid, link: link });
  }

  static getInstance() {
    if (!this.singleton) {
      this.singleton = new GaleWingApi();
    }

    return this.singleton;
  }
}
