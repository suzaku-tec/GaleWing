import { Grid } from 'gridjs';
import axios from 'axios';
import { IElementEvent } from './elementEvent';

export default class UpdateFeed implements IElementEvent {
  uuidElementId: string;
  grid: Grid;

  constructor(uuidElementId: string, grid: Grid) {
    this.uuidElementId = uuidElementId;
    this.grid = grid;
  }

  execute() {
    var uri = new URL(window.location.href);

    var uuid = (<HTMLInputElement>document.getElementById(this.uuidElementId)).value;

    console.log('execute', uri);
    axios
      .post(uri.origin + '/feed/update', {
        uuid: uuid,
      })
      .then((response) => {
        console.log(response);

        // 未読数の更新
        response.data.siteFeedCounts.forEach((element: any) => {
          var countElement = document.getElementById(element.uuid + '_count');
          countElement.innerText = element.count;
        });

        // grid更新
        this.grid
          .updateConfig({
            data: response.data.feeds,
          })
          .forceRender();
      })
      .catch((error) => {
        console.log(error);
      });
  }
}
