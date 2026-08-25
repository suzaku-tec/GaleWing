import axios, { AxiosResponse } from 'axios';

export default class StaticsApi {
  subDomain: string = "/statics/";

  private static singleton: StaticsApi;

  private constructor() { }

  static getInstance() {
    if (!this.singleton) {
      this.singleton = new StaticsApi();
    }

    return this.singleton;
  }

  private getBaseUrl(): string {
    const baseUrl = new URL(window.location.href);
    this.checkUrl(baseUrl);
    return baseUrl.origin + this.subDomain;
  }

  private checkUrl(url: URL) {
    if (url.protocol !== 'https:' && url.protocol !== 'http:') {
      throw new StaticsApiURLError();
    }
  }

  async getReadRate(): Promise<AxiosResponse<{ totalDelivered: number, activeReads: number, opened: number }>> {
    const ajaxUrl = this.getBaseUrl() + "readRate";
    return await axios.post(ajaxUrl);
  }

  async getWordRank(): Promise<AxiosResponse<any>> {
    const ajaxUrl = this.getBaseUrl() + "rank/word";
    return await axios.post(ajaxUrl);
  }
}

class StaticsApiURLError extends Error { }

