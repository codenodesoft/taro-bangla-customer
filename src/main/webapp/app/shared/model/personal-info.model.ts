import { Moment } from 'moment';
import { MaritalStatus } from 'app/shared/model/enumerations/marital-status.model';
import { GenderType } from 'app/shared/model/enumerations/gender-type.model';
import { BloodGroupType } from 'app/shared/model/enumerations/blood-group-type.model';

export interface IPersonalInfo {
  id?: number;
  name?: string;
  banglaName?: string;
  photoContentType?: string;
  photo?: any;
  fatherName?: string;
  motherName?: string;
  maritalStatus?: MaritalStatus;
  spouseName?: string;
  dateOfBirth?: Moment;
  nationalId?: string;
  birthRegistration?: string;
  height?: number;
  gender?: GenderType;
  bloodGroup?: BloodGroupType;
  emergencyContact?: string;
}

export class PersonalInfo implements IPersonalInfo {
  constructor(
    public id?: number,
    public name?: string,
    public banglaName?: string,
    public photoContentType?: string,
    public photo?: any,
    public fatherName?: string,
    public motherName?: string,
    public maritalStatus?: MaritalStatus,
    public spouseName?: string,
    public dateOfBirth?: Moment,
    public nationalId?: string,
    public birthRegistration?: string,
    public height?: number,
    public gender?: GenderType,
    public bloodGroup?: BloodGroupType,
    public emergencyContact?: string
  ) {}
}
