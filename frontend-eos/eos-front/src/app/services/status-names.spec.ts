import { TestBed } from '@angular/core/testing';

import { StatusNames } from './status-names';

describe('StatusNames', () => {
  let service: StatusNames;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StatusNames);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
