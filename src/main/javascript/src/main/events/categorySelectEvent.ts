import GaleWingApi from "../api/galeWingApi";
import { IElementEvent } from "./elementEvent";
import { Grid } from 'gridjs';

export default class CategorySelectEvent implements IElementEvent {
  private grid: Grid;

  constructor(grid: Grid) {
    this.grid = grid;
  }

  execute(): void {
    const categorySelect = document.getElementById('categoryId') as HTMLSelectElement;
    const selectedCategoryId = categorySelect.value;

    const galeWingApi = GaleWingApi.getInstance();
    galeWingApi.getFeedsByCategory(selectedCategoryId).then((res) => {
      this.grid.updateConfig({
        data: res.data,
      }).forceRender();
    });
  }
}
