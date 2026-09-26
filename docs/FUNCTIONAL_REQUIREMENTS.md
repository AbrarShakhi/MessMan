# Functional Requirements

What the app must do. Sources: `docs/DESCRIPTION.md` and the decisions made on 2026-09-26 (section 2).
Where this document had to fill a gap that neither source covers, the requirement is marked with an assumption ID (A1, A2, …). All of them are listed in section 20 for review.

## 1. Terms

- **User**: anyone with an account.
- **Mess**: a group of users who share meals and costs.
- **Member**: a user who belongs to a mess. The owner and the manager are members too.
- **Owner**: the one member who owns the mess. Controls membership and costs.
- **Manager**: the one member who runs meals and periods. At first, the owner is also the manager.
- **Period**: the app's "month". The manager starts and closes it on any day, so it is not a calendar month.
- **Meal time**: a named meal slot in a day, such as Breakfast. A mess has 0–4 of them.
- **Meal count**: how many meals a member takes at one meal time on one day.
- **Grocery entry**: something a member bought for the mess, with its price.
- **Cost**: any other charge the owner sets, such as rent or electricity. Each member it applies to gets a **charge**.
- **Grace period**: the 24 hours after a period closes. Groceries for that period can still be added or changed during it.
- **Final**: a period is final once its grace period has ended. Nothing in it can change after that.

## 2. Decisions

| Topic | Decision |
|---|---|
| Offline use | Only grocery entries can be made offline. Meal counts need a connection. |
| Grace period | Groceries for a closed period can be added or changed for 24 hours after it closes. |
| Default meal count | 1 for every meal time, unless the member changes it. |
| Meal count values | Whole numbers only. |
| Shared costs | For each cost, the owner picks a fixed amount per member or a total split equally. |
| Cost periods | Costs belong to one period. The owner can copy the previous period's costs. |
| Late fines | For each cost, the owner picks a one-time fine or a per-day fine. |
| Manager changes | Only while no period is open. |
| Visibility | Every member sees all data of their mess. |
| Grocery trust | Entries count immediately. Only the buyer can change them, and every change is logged. |
| Sign-in | Email and password only. |
| Languages | English and Bangla. |
| Currency | Chosen per mess. |
| Payment records | Count immediately. The member is notified and doesn't need to confirm. |

## 3. Roles and permissions

Everything in the Member column applies to every member, including the manager and the owner. One person can be both owner and manager.

| Action | Member | Manager | Owner |
|---|:---:|:---:|:---:|
| See all mess data, reports and the activity log | ✓ | ✓ | ✓ |
| Change own meal counts (unlocked meal times, online) | ✓ | ✓ | ✓ |
| Add, edit and delete own grocery entries | ✓ | ✓ | ✓ |
| Agree to or refuse a removal someone else is facing | ✓ | ✓ | ✓ |
| Request to leave | ✓ | | |
| Set meal times; lock and unlock them | | ✓ | |
| Correct any member's meal count (A14) | | ✓ | |
| Start and close periods | | ✓ | |
| Offer the manager role to another member (between periods) | | ✓ | ✓ |
| Record payments for a period's meal balances | | ✓ ¹ | |
| Create, edit and copy costs | | | ✓ |
| Record payments for cost charges | | | ✓ ² |
| Generate and revoke join keys | | | ✓ |
| Approve or reject leave requests | | | ✓ |
| Start or cancel a removal | | | ✓ |
| Offer ownership to another member | | | ✓ |
| Edit mess details; delete the mess when alone in it | | | ✓ |

¹ Recorded by whoever managed that period, even after they have handed over the role.
² Recorded by the owner who created the cost, even after they have transferred ownership (A16).

The owner can't leave without first transferring ownership. The manager can't leave without first handing over the manager role.

## 4. Accounts (FR-ACC)

- **FR-ACC-01** A person can create an account with a name, an email address and a password. They must confirm their email address before they can sign in (A1).
- **FR-ACC-02** A user can sign in with email and password, stay signed in across app restarts, and sign out.
- **FR-ACC-03** A user can reset a forgotten password by email.
- **FR-ACC-04** A user can edit their name and add an optional phone number. Other members see the name and phone number; the email address stays private (A2).
- **FR-ACC-05** A user can delete their account only when they are not a member of any mess. If they are, the app explains why deletion is blocked. After deletion, their old records in messes they left show as "Deleted user" (A3).
- **FR-ACC-06** A signed-in user who isn't in a mess sees two choices: create a mess or join a mess.

## 5. Mess (FR-MESS)

- **FR-MESS-01** A user who isn't in a mess can create one with a name, an optional address and a currency (default BDT). They become its owner and its manager.
- **FR-MESS-02** A user can be a member of at most one mess at a time.
- **FR-MESS-03** The owner can edit the mess name and address. The currency can't change once the first period has started (A4).
- **FR-MESS-04** The owner can delete the mess only when they are its only member. Deleting asks for confirmation and permanently removes the mess's data (A5).
- **FR-MESS-05** Each mess has a time zone, taken from the creator's phone when the mess is created. Days, locks and due dates use it (A6).

## 6. Joining (FR-JOIN)

- **FR-JOIN-01** The owner can generate a random join key and share it, e.g. by copying it or sending it through another app.
- **FR-JOIN-02** A join key works once. It stops working after one person joins with it, 24 hours after it was generated (A7), or when the owner revokes it.
- **FR-JOIN-03** A user who isn't in a mess can enter a join key. If the key is valid, they become a member immediately.
- **FR-JOIN-04** If they join while a period is open, their default meal counts start the day after they join. They can set their own counts for the day they joined (A8).

## 7. Roles (FR-ROLE)

- **FR-ROLE-01** A mess always has exactly one owner and exactly one manager.
- **FR-ROLE-02** The owner can offer ownership to another member. It moves when that member accepts (A9). The previous owner stays a member.
- **FR-ROLE-03** The owner or the manager can offer the manager role to another member, but only while no period is open. It moves when that member accepts (A9). Each period therefore has exactly one manager.
- **FR-ROLE-04** The sender can cancel an offer before it is accepted. An offer is cancelled automatically if either person leaves the mess.

## 8. Leaving and removal (FR-LEAVE)

- **FR-LEAVE-01** A member can request to leave. The owner approves or rejects the request.
- **FR-LEAVE-02** The owner can't leave or be removed; they must transfer ownership first. A member holding the manager role can't leave or be removed until they hand it over.
- **FR-LEAVE-03** A member can only leave or be removed when they owe nothing and are owed nothing. Every period they took part in must be final, and all their meal balances, charges and fines must be settled. The app shows the member what is still outstanding. In practice, members leave between periods.
- **FR-LEAVE-04** The owner can't remove a member directly. The owner can start a removal, and every other member (except the one being removed) is notified and asked to agree. Starting the removal counts as the owner's agreement.
- **FR-LEAVE-05** The member is removed only when everyone asked has agreed and FR-LEAVE-03 is satisfied. One refusal cancels the removal. The owner can also cancel it at any time. There is no deadline for answering (A10).
- **FR-LEAVE-06** A member who leaves or is removed loses access to the mess. Their past records stay in its history.

## 9. Periods (FR-PER)

- **FR-PER-01** The manager can start a period when none is open. It starts on the day it is started.
- **FR-PER-02** A mess has at most one open period. Periods never overlap, and there can be days between two periods.
- **FR-PER-03** The manager can close the open period after confirming. It ends on the day it is closed.
- **FR-PER-04** Closing a period freezes its meal counts immediately.
- **FR-PER-05** For 24 hours after a period closes, members can still add, edit or delete their grocery entries dated inside it. When those 24 hours end, the period becomes final and nothing in it can change.
- **FR-PER-06** Until a period is final, the app shows its meal rate and balances as provisional. When it becomes final, each member's meal balance becomes a debt with the period's manager (FR-PAY-05).
- **FR-PER-07** A closed period can't be reopened (A11).
- **FR-PER-08** A new period can start while the previous one is still in its grace period.

## 10. Meal times and meal counts (FR-MEAL)

- **FR-MEAL-01** The manager sets how many meal times a day has (0–4) and names each one, e.g. Breakfast, Lunch, Dinner. 0 means no meals. Changes apply from the next day (A12).
- **FR-MEAL-02** On every day of an open period, each member's meal count for each meal time is 1 unless they change it.
- **FR-MEAL-03** Meal counts are whole numbers from 0 to 10 (A13). A count of 2 means two meals at that meal time, e.g. for a guest.
- **FR-MEAL-04** A member can change their own count for any unlocked meal time in the open period, and for future days.
- **FR-MEAL-05** A quick entry sets every meal time over a date range at once, e.g. 0 while the member is away. Counts set for future dates apply to whichever period contains those dates.
- **FR-MEAL-06** Changing a meal count needs an internet connection. The server rejects the change if that meal time is locked or the day belongs to a closed period, and the app says why.
- **FR-MEAL-07** The manager can lock and unlock a meal time on a given day. While it is locked, members can't change their counts for it.
- **FR-MEAL-08** The manager can correct any member's count in the open period, including for locked meal times (A14).
- **FR-MEAL-09** For any day, the app shows every member's counts and the total per meal time, so the cook knows how many meals to prepare.
- **FR-MEAL-10** Members can browse the daily meal records of the current and past periods, both their own and everyone else's.

## 11. Groceries (FR-GRO)

- **FR-GRO-01** Any member can add a grocery entry with:
  - item name
  - quantity and unit (kg, g, litre, piece, dozen, or their own unit)
  - total price, with up to 2 decimal places
  - purchase date (default today)
  
  The member who adds it is recorded as the buyer.
- **FR-GRO-02** The purchase date decides which period the entry belongs to. The date must fall inside the open period, or inside a closed period that is still in its grace period (A15).
- **FR-GRO-03** Entries count toward the meal rate immediately; nobody has to approve them.
- **FR-GRO-04** Only the buyer can edit or delete an entry, and only until its period becomes final. Every change appears in the activity log.
- **FR-GRO-05** A member can add grocery entries while offline. The app saves them on the phone, marks them as waiting to sync, and uploads them automatically when the connection returns. Until an entry is uploaded, the buyer can also edit or delete it on the phone. Changing an entry that is already uploaded needs a connection.
- **FR-GRO-06** The server rejects an uploaded entry if its period has already become final. The buyer is told which entry was rejected and why, and it doesn't count.
- **FR-GRO-07** Members can see each period's grocery entries, with totals per member and for the whole mess.

## 12. Meal rate and balances (FR-CALC)

- **FR-CALC-01** The meal rate of a period is the total price of all its grocery entries divided by the total of all members' meal counts in it. If the total meal count is 0, the meal rate is 0 (A20).
- **FR-CALC-02** A member's meal cost is their total meal count in the period multiplied by the meal rate.
- **FR-CALC-03** A member's meal balance is the total price of the groceries they bought in the period minus their meal cost. Below 0, they owe the manager. Above 0, the manager owes them.
- **FR-CALC-04** Calculations use full precision. Money is rounded to 2 decimal places only for display and for the final debts.

Example:

| Member | Groceries bought | Meals | Meal cost | Balance |
|---|---:|---:|---:|---:|
| A (manager) | 3,000.00 | 60 | 1,600.00 | +1,400.00 |
| B | 1,000.00 | 50 | 1,333.33 | −333.33 |
| C | 0.00 | 40 | 1,066.67 | −1,066.67 |
| **Total** | **4,000.00** | **150** | **4,000.00** | **0.00** |

- The meal rate is 4,000 ÷ 150 = 26.67 (26.666…).
- B owes A 333.33, and C owes A 1,066.67.
- When both pay, A receives 1,400.00, which is exactly A's own positive balance. So the manager's own balance never becomes a separate debt.

## 13. Costs (FR-COST)

- **FR-COST-01** The owner can add costs to the open period (A17), e.g. rent, electricity or internet. Each cost has:
  - a name and an amount
  - the members it applies to (everyone or chosen members)
  - how it is charged
  - a due date
  - an optional late fine
- **FR-COST-02** For each cost, the owner picks how it is charged:
  - a fixed amount for each chosen member (rent 2,500 each), or
  - a total split equally among the chosen members (electricity 3,000 ÷ 6 = 500 each).
  
  Split shares are rounded to 2 decimal places and shown before saving.
- **FR-COST-03** For each cost, the owner picks the late fine: none, a fixed amount added once, or an amount added for each day late.
- **FR-COST-04** Each chosen member gets a charge for the cost, payable to the owner who created it (A16).
- **FR-COST-05** The owner can edit or delete a cost later. Payments already recorded against it are kept, and the amount outstanding is recalculated.
- **FR-COST-06** When a new period starts, the owner can copy the previous period's costs into it, then adjust the amounts, members and due dates.
- **FR-COST-07** If a charge isn't fully paid by the end of its due date:
  - a one-time fine is added once;
  - a per-day fine is added for each further day, until the charge is fully paid.

## 14. Payments and balances (FR-PAY)

- **FR-PAY-01** The app never moves money. It only records payments that members make to each other outside the app.
- **FR-PAY-02** The person on the mess's side of the money records each payment:
  - the owner who created a cost records payments for its charges;
  - a period's manager records payments for that period's meal balances, even after handing over the role.
  
  A payment has a direction (the member paid me, or I paid the member), an amount, a date, what it is for, and an optional note.
- **FR-PAY-03** A payment counts as soon as it is recorded. The member is notified and it appears in the activity log; the member doesn't confirm it.
- **FR-PAY-04** The person who recorded a payment can edit or delete it to fix a mistake. The member is notified again (A18).
- **FR-PAY-05** When a period becomes final, each member's meal balance becomes a debt with that period's manager, as in the example in section 12. The manager's own balance is not a debt.
- **FR-PAY-06** Debts stay between the same two people when roles change later. Example from the description: A is manager in May, and B owes A for May. C becomes manager in June. B still owes A.
- **FR-PAY-07** A payment can't be larger than what is still outstanding for the thing it pays (A19).
- **FR-PAY-08** Each member can see what they owe and what they are owed:
  - each period's meal balance
  - each charge, with its fines and payments
  - a total per person

## 15. Notifications (FR-NOT)

A member gets a push notification, also kept in an in-app notification list (A21), when:

- **FR-NOT-01** a payment involving them is recorded, edited or deleted;
- **FR-NOT-02** one of their charges is due tomorrow and isn't fully paid (A22), or a late fine is added to it;
- **FR-NOT-03** the owner starts a removal they are asked to agree to;
- **FR-NOT-04** someone offers them the owner or manager role, or (for the owner) a member requests to leave;
- **FR-NOT-05** a period starts or closes (A23);
- **FR-NOT-06** one of their offline grocery entries is rejected (FR-GRO-06).

The original feature list mentions reminders for missing entries. Because every meal count defaults to 1 (FR-MEAL-02), a meal entry is never missing, so there are no missing-entry reminders.

## 16. Activity log (FR-LOG)

- **FR-LOG-01** Every change in a mess is logged with who made it, when, and the old and new values. This covers:
  - meal counts, locks and meal-time settings
  - grocery entries, costs and payments
  - periods and roles
  - joins, leave requests and removals
  - automatic events such as late fines and rejected entries
- **FR-LOG-02** Every member can read the whole log of their mess. Nobody can edit or delete log entries.

## 17. Reports and charts (FR-REP)

- **FR-REP-01** For each period, current or past, a member can see:
  - the period's dates, meal rate, total groceries and total meals
  - for every member: meals, groceries bought, meal cost, balance, charges, fines and payments
- **FR-REP-02** The app shows these charts (A24):
  - meal rate per period
  - total grocery spending per period
  - each member's meals per period
  - grocery spending by member within a period

## 18. Sync and offline (FR-SYNC)

- **FR-SYNC-01** While online, changes made by any member appear on other members' screens automatically, without refreshing.
- **FR-SYNC-02** Data loaded earlier can be viewed offline, with a note saying when it was last updated.
- **FR-SYNC-03** Offline, only grocery entries can be created (FR-GRO-05). Other actions tell the user that they need a connection.
- **FR-SYNC-04** Entries waiting to sync are kept through app restarts and upload automatically when the connection returns.

## 19. Settings (FR-SET)

- **FR-SET-01** A user can choose the app language: English or Bangla. By default, it follows the phone's language.
- **FR-SET-02** A user can choose a light, dark or system theme.

## 20. Assumptions to confirm

| ID | Assumption | Used in |
|---|---|---|
| A1 | Users must confirm their email address before the first sign-in. | FR-ACC-01 |
| A2 | Members see each other's name and phone number, but not email addresses. | FR-ACC-04 |
| A3 | After account deletion, past records show "Deleted user". | FR-ACC-05 |
| A4 | A mess's currency can't change once its first period has started. | FR-MESS-03 |
| A5 | Deleting a mess permanently deletes its data. | FR-MESS-04 |
| A6 | A mess uses the time zone of its creator's phone. | FR-MESS-05 |
| A7 | Join keys expire 24 hours after they are generated. | FR-JOIN-02 |
| A8 | A member who joins mid-period gets default meal counts from the next day. | FR-JOIN-04 |
| A9 | Ownership and manager-role transfers need the receiving member to accept. | FR-ROLE-02, 03 |
| A10 | A removal has no deadline; one refusal cancels it. | FR-LEAVE-05 |
| A11 | A closed period can't be reopened. | FR-PER-07 |
| A12 | Meal-time changes apply from the next day. | FR-MEAL-01 |
| A13 | A meal count is at most 10. | FR-MEAL-03 |
| A14 | The manager can correct any member's meal count, including for locked meal times. | FR-MEAL-08 |
| A15 | Groceries dated outside every period (e.g. between two periods) can't be added. | FR-GRO-02 |
| A16 | A cost's charges are payable to the owner who created it, even after ownership changes. | FR-COST-04 |
| A17 | Costs can only be added to the open period. | FR-COST-01 |
| A18 | A recorded payment can be edited or deleted by whoever recorded it. | FR-PAY-04 |
| A19 | A payment can't be larger than what is outstanding. | FR-PAY-07 |
| A20 | A period with no meals has a meal rate of 0. | FR-CALC-01 |
| A21 | The app keeps an in-app list of notifications. | FR-NOT |
| A22 | Due-date reminders are sent one day before the due date. | FR-NOT-02 |
| A23 | Members are notified when a period starts or closes. | FR-NOT-05 |
| A24 | The first set of charts is the four in FR-REP-02. | FR-REP-02 |
