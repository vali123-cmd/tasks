import { TestBed } from '@angular/core/testing';

import { Adminguard } from './adminguard';

describe('Adminguard', () => {
  let service: Adminguard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Adminguard);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
