import GaleWingGrid from '../screen/feed/galeWingGrid';
import GaleWingApi from '../api/galeWingApi';
import { IElementEvent } from './elementEvent';

export default class ReadDispListEvent implements IElementEvent {
  execute(): void {
    let gridjsTdList = Array.from(document.getElementsByClassName('gridjs-td'));

    GaleWingGrid.getInstance().setStopRowClickFlg(true);

    Promise.all(
      gridjsTdList
        .map((td) => td.querySelector('a.rss-link'))
        .filter((el) => el !== null)
        .map((el) => <HTMLAnchorElement>el)
        .map((anchorEl) => {
          GaleWingApi.getInstance()
            .read(anchorEl.href)
            .then(() => {
              // 既読表示に変更
              anchorEl.parentElement?.click();
            })
            .catch((error) => {
              console.log(error);
            });
        }),
    )
      .then(() => {
        GaleWingGrid.getInstance().setStopRowClickFlg(false);
      })
      .catch(() => {
        GaleWingGrid.getInstance().setStopRowClickFlg(false);
      });
  }
}
