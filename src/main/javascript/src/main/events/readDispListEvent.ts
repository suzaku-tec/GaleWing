import GaleWingApi from '../api/galeWingApi';
import GaleWingGrid from '../screen/feed/galeWingGrid';
import { IElementEvent } from './elementEvent';

export default class ReadDispListEvent implements IElementEvent {
  execute(): void {
    const gridApi = GaleWingGrid.getInstance().gridApi;
    gridApi.getRenderedNodes()
      .forEach((node) => {
        const link = node.data.link;
        const currentValue = node.getDataValue('readed');
        node.setDataValue('readed', currentValue ? false : true);
        GaleWingApi.getInstance().read(link)
          .then(() => {
            console.log('GaleWingGrid onCellClicked read success:', link);
            GaleWingGrid.getInstance().gridApi.refreshCells({
              rowNodes: [node],
              force: true,
              columns: ['title'],
            });
          })
          .catch((error) => {
            console.error('GaleWingGrid onCellClicked read error:', error);
          });

      });

  }
}
