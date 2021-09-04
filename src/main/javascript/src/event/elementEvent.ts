export default class ElementEvent {
  private executor: IElementEvent;

  constructor(executor: IElementEvent) {
    this.executor = executor;
  }

  setup<K extends keyof HTMLElementEventMap>(type: K, element: HTMLElement) {
    element.addEventListener(type, () => this.executor.execute());
  }
}

export interface IElementEvent {
  execute(): void;
}
