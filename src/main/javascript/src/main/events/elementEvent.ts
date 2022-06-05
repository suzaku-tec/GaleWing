export default class ElementEvent {
  private executor: IElementEvent;

  constructor(executor: IElementEvent) {
    this.executor = executor;
  }

  setup<K extends keyof HTMLElementEventMap>(type: K, element: HTMLElement | null) {
    element?.addEventListener(type, () => this.executor.execute());
  }
}

export interface IElementEvent {
  /**
   * イベント実行
   */
  execute(): void;
}
