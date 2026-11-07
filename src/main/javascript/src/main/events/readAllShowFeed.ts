import axios from 'axios';
import { IElementEvent } from './elementEvent';
import { getSite } from '../screen/siteList';

export default class ReadAllShowFeed implements IElementEvent {

  execute(): void {
    const element = <HTMLInputElement>document.getElementById('identifier');
    const identifier = element.value;
    const uri = new URL(window.location.href);
    console.log('identifier: ', identifier);

    axios
      .post(uri.origin + '/readAllShowFeed', {
        identifier: identifier,
      })
      .catch((error) => console.error(error))
      .finally(() => {
        const site = getSite(identifier);
        if (site && site.next) {
          const anchor = <HTMLAnchorElement>site.next;
          location.href = anchor.href;
        } else {
          location.reload();
        }
      });
  }

  updateSiteFeedCount(data: [{ uuid: string; title: string; count: number }]) {
    data.forEach((d) => {
      const cntEl = document.getElementById(d.uuid + '_count')!;
      cntEl.innerText = d.count.toString();
    });
  }
}
