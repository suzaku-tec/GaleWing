import axios, { AxiosResponse } from 'axios';

export default class GaleWingApi {
  private readonly baseUrl: URL;

  private readonly apiUrls = {
    feedList: '/feedlist',
    siteList: '/sitelist',
    deleteSite: '/site/delete',
  };

  constructor() {
    this.baseUrl = new URL(window.location.href);
  }

  getFeedList() {
    var ajaxUrl = this.baseUrl.origin + this.apiUrls.feedList + location.search;
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

  private static singleton: GaleWingApi;
  static getInstance() {
    if (!this.singleton) {
      this.singleton = new GaleWingApi();
    }

    return this.singleton;
  }
}
