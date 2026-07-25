import Grid from "gridjs";
import { IElementEvent } from "./elementEvent";
import GaleWingApi from "../api/galeWingApi";

export default class CirculationEvent implements IElementEvent {
  private grid: Grid;
  private limit: number;

  constructor(grid: Grid, limit: number) {
    this.grid = grid;
    this.limit = limit;
  }

  execute(): void {
    const targets = [];
    const data = this.grid.config.store.getState().data as any;
    for (let i = 0; i < data.length; i++) {
      if (data._rows[i]._cells[0].data) {
        targets.push(data.rows[i]);
      }
    }

    const api = GaleWingApi.getInstance();
    targets.forEach(target => {
      api.circulationAdd(target._cells[2].data, target._cells[1].data)
    });
  }

}
