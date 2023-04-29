import GaleWingApi from '../api/galeWingApi';
import FeedApi from '../api/disp/feedApi';
import { IElementEvent } from './elementEvent';
import axios from 'axios';

export default class ReadDispListEvent implements IElementEvent {
  execute(): void {
    let gridjsTdList = Array.from(document.getElementsByClassName('gridjs-td'));

    let uri = new URL(window.location.href);

    gridjsTdList
      .map((td) => td.querySelector('a.rss-link'))
      .filter((el) => el !== null)
      .map((el) => <HTMLAnchorElement>el)
      .forEach((anchorEl) => {
        GaleWingApi.getInstance()
          .read(anchorEl.href)
          .then(() => {
            // 既読表示に変更
            anchorEl.classList.remove('rss-link');
            anchorEl.classList.add('rss-read-link');
          })
          .catch((error) => {
            console.log(error);
          });
      });
  }
}
