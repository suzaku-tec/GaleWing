
type modifierKey = "ctrl" | "shift" | "alt";
type char = 'a' | 'b' | 'c' | 'd' | 'e' | 'f' | 'g' | 'h' | 'i' | 'j' | 'k'
  | 'l' | 'm' | 'n' | 'o' | 'p' | 'q' | 'r' | 's' | 't' | 'u' | 'v' | 'w' | 'x'
  | 'y' | 'z' | '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9';

export default class SuperCut {
  private shortcutKey = { def: { ctrl: {}, shift: {}, alt: {} } };

  constructor() {
    this.shortcutKey.def = {
      ctrl: {},
      shift: {},
      alt: {},
    };
  }


  init() {

  }

  addShortCut(modifierKey: modifierKey, key: char, func: () => void) {
    if (modifierKey === "alt") {
      if (!this.shortcutKey.def.alt.hasOwnProperty(key)) {
        Object.assign(this.shortcutKey.def.alt, { key: func });
      }
    } else if (modifierKey === "ctrl") {
      if (!this.shortcutKey.def.ctrl.hasOwnProperty(key)) {
        Object.assign(this.shortcutKey.def.ctrl, { key: func });
      }
    } else if (modifierKey === "shift") {
      if (!this.shortcutKey.def.shift.hasOwnProperty(key)) {
        Object.assign(this.shortcutKey.def.shift, { key: func });
      }
    }
  }

}

