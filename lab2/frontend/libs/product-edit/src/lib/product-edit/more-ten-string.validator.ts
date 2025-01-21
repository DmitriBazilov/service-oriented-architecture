import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export function moreTenStringValidator(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;

    if (value !== null && value.length > 0 && value.length < 10) {
      return { 'noEmptyString': true };
    }

    return null;
  };
}