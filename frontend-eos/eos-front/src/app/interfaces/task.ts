export interface Task {
        id: number,
        content: string,
        dueDate: string,
        statusName: string,
        AssignedTo: number | null,
        createdBy: string,
        creationDate: string
}