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
        axios
          .post(uri.origin + '/read', {
            link: anchorEl.href,
          })
          .then((response) => {
            // 未読数の更新
            response.data.forEach((element: { uuid: string; count: number }) => {
              let countElement = <HTMLInputElement>document.getElementById(element.uuid + '_count');
              countElement.innerText = element.count.toString();
            });

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
