export class ClientRequest {
    constructor(
        public clientID: number = 0,
        public firstName: string = '',
        public middleName: string | null = null,
        public lastName: string = '',
        public secondLastName: string | null = null,
        public emailAddress: string | null = null,
        public phoneNumber: string | null = null,
        public startDate: Date | null = null,
        public endDate: Date | null = null
    ) {}
}