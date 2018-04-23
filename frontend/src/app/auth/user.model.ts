export class User {
  constructor(public email: string,
              public password: string,
              public developer: boolean,
              public name?: string,
              public id?: number) {
  }
}
