export type ApiPagePart = {
  size: number,
  number: number,
  totalElements: number,
  totalPages: number,
}

export type ApiPageResponse<T> = {
  content: T,
  page: ApiPagePart
}
