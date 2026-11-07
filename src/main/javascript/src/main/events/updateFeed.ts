import axios from 'axios';
import { IElementEvent } from './elementEvent';

import UpdateMessageEvent from './modal/updateMessageEvent';
import { GridApi } from 'ag-grid-community';
export default class UpdateFeed implements IElementEvent {
  uuid: string;
  gridApi: GridApi;

  constructor(uuid: string, gridApi: GridApi) {
    this.uuid = uuid;
    this.gridApi = gridApi;
  }

  execute() {
    const ume = new UpdateMessageEvent();
    ume.execute();

    const uri = new URL(window.location.href);

    console.log('execute', uri);
    axios
      .post(uri.origin + '/feed/update', {
        uuid: this.uuid,
      })
      .then((response) => {
        console.log(response);

        // 未読数の更新
        response.data.siteFeedCounts.forEach((element: any) => {
          const countElement = document.getElementById(element.uuid + '_count');
          countElement!.innerText = element.count;
        });

        // grid更新
        this.gridApi!.setGridOption('rowData', response.data);
      })
      .catch((error) => {
        console.log(error);
      })
      .finally(() => ume.close());
  }
}
