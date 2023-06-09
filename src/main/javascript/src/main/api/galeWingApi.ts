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
