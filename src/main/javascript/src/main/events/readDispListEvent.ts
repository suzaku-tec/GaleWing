import GaleWingApi from '../api/galeWingApi';
import GaleWingGrid from '../screen/feed/galeWingGrid';
import { IElementEvent } from './elementEvent';

export default class ReadDispListEvent implements IElementEvent {
  execute(): void {
    let gridjsTdList = Array.from(document.getElementsByClassName('gridjs-td'));

    GaleWingGrid.getInstance().setStopRowClickFlg(true);

    var urls = gridjsTdList
      .map((td) => td.querySelector('a.rss-link'))
      .filter((el) => el !== null)
      .map((el) => <HTMLAnchorElement>el)
      .map((anchorEl) => {
        // 既読表示に変更
        anchorEl.classList.remove('rss-link');
        anchorEl.classList.add('rss-read-link');
        return anchorEl.href;
      });

      GaleWingApi.getInstance().readDispList(urls);

      GaleWingGrid.getInstance().setStopRowClickFlg(false);
  }
}
