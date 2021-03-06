import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CodeNodeErpSharedModule } from 'app/shared/shared.module';
import { LeaveApplicationComponent } from './leave-application.component';
import { LeaveApplicationDetailComponent } from './leave-application-detail.component';
import { LeaveApplicationUpdateComponent } from './leave-application-update.component';
import { LeaveApplicationDeleteDialogComponent } from './leave-application-delete-dialog.component';
import { leaveApplicationRoute } from './leave-application.route';
import { LeaveApplicationReviewFirstAuthorityComponent } from 'app/entities/leave-application/leave-application-review-first-authority.component';
import { OtherLeaveApplicationUpdateComponent } from 'app/entities/leave-application/other-leave-application-update.component';
import { OtherLeaveApplicationComponent } from 'app/entities/leave-application/other-leave-application.component';
import { LeaveApplicationReviewSecondAuthorityComponent } from 'app/entities/leave-application/leave-application-review-second-authority.component';
import { LeaveApplicationActionBySecondAuthorityUpdateComponent } from 'app/entities/leave-application/leave-application-action-by-second-authority-update.component';
import { LeaveApplicationActionByFirstAuthorityUpdateComponent } from 'app/entities/leave-application/leave-application-action-by-first-authority-update.component';

@NgModule({
  imports: [CodeNodeErpSharedModule, RouterModule.forChild(leaveApplicationRoute)],
  declarations: [
    LeaveApplicationComponent,
    LeaveApplicationDetailComponent,
    LeaveApplicationUpdateComponent,
    LeaveApplicationDeleteDialogComponent,
    LeaveApplicationReviewFirstAuthorityComponent,
    LeaveApplicationReviewSecondAuthorityComponent,
    LeaveApplicationActionByFirstAuthorityUpdateComponent,
    LeaveApplicationActionBySecondAuthorityUpdateComponent,
    OtherLeaveApplicationComponent,
    OtherLeaveApplicationUpdateComponent,
  ],
  entryComponents: [LeaveApplicationDeleteDialogComponent],
})
export class CodeNodeErpLeaveApplicationModule {}
