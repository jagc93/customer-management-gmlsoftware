export class PageResponse<T> {
    content!: T[];
    number!: number;
    size!: number;
    totalElements!: number;
    empty!: boolean;
    notEmpty!: boolean;
}
