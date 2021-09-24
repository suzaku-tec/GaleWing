import axios from 'axios';
import { IElementEvent } from './elementEvent';
import { Grid } from 'gridjs';
import { getSite } from '../siteList';

export default class ReadAllShowFeed implements IElementEvent {
  private grid: Grid;

  constructor(grid: Grid) {
    this.grid = grid;
  }

  execute(): void {
    var element = <HTMLInputElement>document.getElementById('identifier');
    var identifier = element.value;
    var uri = new URL(window.location.href);
    console.log('identifier: ', identifier);

    axios
      .post(uri.origin + '/readAllShowFeed', {
        identifier: identifier,
      })
      .then((response) => {
        // this.updateSiteFeedCount(response.data);
        // this.grid
        //   .updateConfig({
        //     data: [],
        //   })
        //   .forceRender();
        return;
      })
      .catch((error) => {})
      .finally(() => {
        var site = getSite(identifier);
        console.log(site);
        if (site.next) {
          var anchor = <HTMLAnchorElement>site.next;
          location.href = anchor.href;
        }
      });
  }

  updateSiteFeedCount(data: [{ uuid: string; title: string; count: number }]) {
    data.forEach((d) => {
      var cntEl = document.getElementById(d.uuid + '_count');
      cntEl.innerText = d.count.toString();
    });
  }
}
