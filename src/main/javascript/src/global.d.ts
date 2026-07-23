declare module '*.css' {
  const content: { [className: string]: string };
  export default content;
}

declare namespace google {
  export namespace charts {
    export function load(version: string, options?: { packages?: string[]; language?: string; callback?: () => void }): void;
    export function setOnLoadCallback(drawChart: (rows: any[]) => void): void;
  }
  export namespace visualization {
    export class DataTable {
      constructor(opt_data?: DataTableData, opt_version?: number);
      addColumn(type: string | ColumnSpec, label?: string, id?: string): number;
      addRow(row?: any[]): number;
      addRows(rows?: any[][]): number;
      setCell(rowIndex: number, columnIndex: number, value?: any, formattedValue?: string, properties?: any): void;
      getRow(columnIndex: number): number;
      getFormattedValue(rowIndex: number, columnIndex: number): string;
      toJSON(): string;
    }

    export interface ColumnSpec {
      type: string;
      label?: string;
      id?: string;
    }

    export interface DataTableData {
      cols: ColumnSpec[];
      rows: { c: { v: any; f?: string }[] }[];
    }

    // チャートオプションのベース型
    export interface ChartOptions {
      width?: number;
      height?: number;
      title?: string;
      legend?: any;
      hAxis?: any;
      vAxis?: any;
      colors?: string[];
      [key: string]: any;
    }

    // ベースチャートクラス
    export abstract class ChartBase {
      constructor(container: HTMLElement);
      draw(data: DataTable | DataView, options?: ChartOptions): void;
      getSelection(): Selection[];
      setSelection(selection?: Selection[]): void;
      getChartType(): string;
      getContainer(): HTMLElement;
    }

    // ColumnChart
    export class ColumnChart extends ChartBase {
      constructor(container: HTMLElement);
    }

    // PieChart
    export class PieChart extends ChartBase {
      constructor(container: HTMLElement);
    }

    // 他によく使うチャート（必要に応じて追加）
    export class LineChart extends ChartBase {
      constructor(container: HTMLElement);
    }

    export class BarChart extends ChartBase {
      constructor(container: HTMLElement);
    }

    // 選択範囲
    export interface Selection {
      row: number | null;
      column: number | null;
    }

    // DataView（必要に応じて）
    export class DataView {
      constructor(data: DataTable);
      // 必要なメソッドを追加
    }

  }
}

declare module "gridjs" {
  export class Grid {
    constructor(config: any);
    render(element: HTMLElement | null): void;
    on(eventName: string, callback: (event: Event, ...args: any[]) => void): void;
  }

  export class Row {
    constructor(cells?: any[]);
    addCell(cell: any): Row;
    cells: Cell[];
  }

  export function html(template: string, ...args: any[]): any;

  export function h(
    tag: string | Function,
    props?: { [key: string]: any } | null,
    ...children: any[]
  ): any;

  export default Grid;
}
