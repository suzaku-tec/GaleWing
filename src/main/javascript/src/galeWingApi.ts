import axios from 'axios';

export default class GaleWingApi {
  private readonly baseUrl: URL;

  private readonly apiUrls = {
    feedList: '/feedlist',
  };

  constructor() {
    this.baseUrl = new URL(window.location.href);
  }

  getFeedList() {
    var ajaxUrl = this.baseUrl.origin + this.apiUrls.feedList + location.search;
    return axios.get(ajaxUrl);
  }
}
