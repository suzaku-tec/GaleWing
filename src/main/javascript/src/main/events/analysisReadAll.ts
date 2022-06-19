import { IElementEvent } from './elementEvent';
import GaleWingApi from '../api/galeWingApi';

export default class AnalysisReadAll implements IElementEvent {
  execute(): void {
    let linkList = Array.from(
      document.getElementsByClassName('rss-link') as HTMLCollectionOf<HTMLElement>,
    ).map((rssLink) => {
      return rssLink.dataset.link!;
    });

    let target = document.getElementById('target') as HTMLAnchorElement;
    linkList.push(target.href);

    GaleWingApi.getInstance()
      .analysisFeedAllRead(linkList)
      .then(() => {
        Array.from(
          document.getElementsByClassName('rss-link') as HTMLCollectionOf<HTMLElement>,
        ).forEach((array) => {
          array.classList.remove('rss-link');
          array.classList.add('rss-read-link');
        });
      })
      .catch((error) => {
        console.log(error);
      });
  }
}
