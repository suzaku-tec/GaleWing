import GaleWingApi from '../api/galeWingApi';
import GaleWingGrid, { HeaderIndex } from '../screen/feed/galeWingGrid';
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

        const link = anchorEl.href;
        const data = GaleWingGrid.getInstance().grid?.config.data as any;
        data.filter((feed: any) => feed[HeaderIndex.link] === link).forEach((feed: any) => {
          feed[HeaderIndex.chkSts] = '1';
        });

        anchorEl.classList.remove('rss-link');
        anchorEl.classList.add('rss-read-link');
        return anchorEl.href;
      });

    GaleWingApi.getInstance().readDispList(urls);
    let targetBadge = <HTMLSpanElement>document.querySelector('a.bg-warning span.badge');
    const cnt = targetBadge?.innerText;
    if (Number(cnt)) targetBadge.innerText = String(Number(cnt) - urls.length);

    GaleWingGrid.getInstance().setStopRowClickFlg(false);

  }
}
