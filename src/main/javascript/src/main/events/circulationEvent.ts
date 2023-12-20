import { Grid } from "gridjs";
import { IElementEvent } from "./elementEvent";
import GaleWingApi from "../api/galeWingApi";

export default class CirculationEvent implements IElementEvent {
  private grid: Grid;
  private limit: Number;

  constructor(grid: Grid, limit: Number) {
    this.grid = grid;
    this.limit = limit;
  }

  execute(): void {
    var targets = [];
    var data = this.grid.config.store.getState().data as any;
    for(var i = 0; i < data.length; i++) {
      if(data._rows[i]._cells[0].data) {
        targets.push(data.rows[i]);
      }
    }

    var api = GaleWingApi.getInstance();
    targets.forEach(target => {
      api.circulationAdd(target._cells[2].data, target._cells[1].data)
    });
  }

}
