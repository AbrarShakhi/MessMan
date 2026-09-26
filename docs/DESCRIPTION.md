## Short Description
The Mess Management App is a smart, automated system designed to streamline meal tracking, expense handling,
and monthly settlements in shared living environments. It reduces manual errors, improves transparency,
and ensures fair, data-driven distribution of costs among members.

## Description
User can create account. Initially if they create account they have not joined a mess.
They will have two options, create a mess, join a mess.
User can create mess. When they create mess they are owner of this mess. Mess can have exactly one owner.
They can transfer ownership to other person.
Each mess will have 1 manager, initially owner is manager. Manager and oner can transfer manager role,
A user can only be member of maximum one mess, but mess can have many members.
If user wants to join a mess, They can enter a random join key provided by owner. key will be used once per use.
Member can not leave, they can request to leave mess, Owner have to confirm it.
Owner can not remove forcefully a member, all member except removing member has to agree to remove him.
If Owner tries to remove a message will be sent to all members.
Owner can not delete mess, all user except Owner have to leave to delete the mess.
Member can not leave or removed if they have any due or owes money.
User can not delete their account if they are part of any mess.
App will have charts, monthly visualizations, Analytics for data-driven decisions.
And payment Tracking to monitor individual balances.

#### Daily Meal Tracking with quick-entry options
There are 3 roles, member, manager, owner. each have different permissions.
all user who joined the mess considered member, even owner and manager.
Manager will set how many times Meal will be every day. they can set between 0-4.
Manager also can name each meal time like breakfast, dinner, etc.
Members can entry each mealtime how many meals count they want, default is 1,
A member entry that breakfast mealtime they want 2 meal count, then his meal count will be 2 for that meal time.
Manager can lock/unlock mealtime and member can not change their meal count.
So that member can not change meal count later after meal has been done.
Member can see their past daily meals records.

#### Expense Logging for groceries, utilities, and other shared costs
Any member can see add groceries, Let's say they buy a food, they will entry how much they bought(kg, pounds, peaces, etc.), with price.
Members grocery cost will be summation of its all grocery cost that they bought.
Each member will have more costs. Owner can set them. Owner can set multiple of them and edit them later.
Owner can set cost for everyone, or individuals.
App will not provide any payment mechanism, Since, mess members are lives together.
They can set expire date, So if someone is late, they have to fine some amount money.

#### Automated Meal Rate & Bill Calculation
Meal rate = summation of all grocery cost of everyone at that month / summation of total meal count of everyone at that month.
Individual Meal cost = Meal rate at that month * (total mean count of that person at that month).
A persons meal cost will be due (that means they have to give money to that manager),
if (Total grocery cost of that person at that month) - (that persons total meal cost that month) < 0.
A person meal cost will be owed (That means manager have to give money to that person),
if (Total grocery cost of that person at that month) - (that persons total meal cost that month) > 0.
obviously money have to exchange in absolute value.
Because mess manager/owner can be changed. So, If a person owes money from someone,
they will still own from other person if manager/onwer changes or.
Example: 'A' is manager. 'A' owns money from 'B'. Next month 'C' becomes manager. 'A' will still ows from 'B'

#### Monthly Reports
Member can visit past month data. (activity logs)
a month is not an exact month from year.
Manager will start a month and close a month.
Example: past month finishes in 8th may, new month started on 10th may, new month closed in 27th May.
If a month is closed, members can not entry meal count. But, they can entry grocery before 24 hours.
but member/manager can pay/manage money.
owner will set that someone has given him money, or owner wil give them money and set that given this amount.
manager can do the same thing.

#### Notifications & Reminders for pending payments or missing entries.
User will be notified, if manager/owner sets their money.
They will get remember before expire date so that they don't get fined.

#### Secure Cloud Backup & Sync across devices.
Data will be stored in clouds.
Member can entry grocery offline. when they go offline they should be synced. 
edge-case: member entered grocery offline, manager closed month, grocery tries to sync. it will be rejected.
Real-time Synchronization: Ensuring all members and admins see updated meal and expense data instantly across multiple devices.
Caching/buffering data in local storage such that users can view/update records even if the internet is unavailable for certain operation.
Synchronize any buffered data once the internet connection is available.
Providing different access levels while ensuring data privacy and preventing unauthorized modifications.
Dynamically computing meal rates and individual dues based on fluctuating expenses and varying meal counts.