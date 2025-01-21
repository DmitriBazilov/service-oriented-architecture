import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function noEmptyStringValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;

    if (value === null || value === '') {
      return { 'noEmptyString': true };
    }

    return null;
  };
}