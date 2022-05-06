export default class Book {
  id;
  isbn;
  title;
  description;
  price;
  pageCount;
  language;
  rating;
  rateCount;
  publishDate;
  authors = [];
  tags = [];

  constructor(id, isbn, title, description, author, tags, price, pageCount, language, rate, rateCount, publishDate) {
    this.id = id;
    this.isbn = isbn;
    this.title = title;
    this.description = description;
    this.author = author;
    this.tags = tags;
    this.price = price;
    this.pageCount = pageCount;
    this.languge = language;
    this.rate = rate;
    this.rateCount = rateCount;
    this.publishDate = publishDate;
  }
}
