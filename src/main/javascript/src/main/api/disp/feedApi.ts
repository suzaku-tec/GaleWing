export default class FeedApi {
  private static singleton: FeedApi;

  private constructor() { }

  static getInstance() {
    if (!this.singleton) {
      this.singleton = new FeedApi();
    }

    return this.singleton;
  }

  unreadUpdate(uuid: string, count: string) {
    const countElement = <HTMLInputElement>document.getElementById(uuid + '_count');
    if (!countElement) {
      console.error(`Element with id ${uuid}_count not found. update skipped.`);
      return;
    }
    countElement.innerText = count;
  }
}
