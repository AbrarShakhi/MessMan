# MessMan: System Description

The Mess Management App is a smart, automated system for meal tracking, expense handling and monthly settlements in shared living environments (a "mess"). It reduces manual errors, improves transparency, and makes sure costs are shared fairly among members, based on data.

The numbered requirements, with the decisions made while writing them, are in [FUNCTIONAL_REQUIREMENTS.md](FUNCTIONAL_REQUIREMENTS.md) and [NON_FUNCTIONAL_REQUIREMENTS.md](NON_FUNCTIONAL_REQUIREMENTS.md).

## Accounts

- Anyone can create an account. A new account doesn't belong to a mess yet.
- A user without a mess has two options: create a mess or join one.
- A user can't delete their account while they are part of a mess.

## Roles

- There are three roles: member, manager and owner. Each has different permissions.
- Everyone who joins a mess is a member, including the owner and the manager.
- The user who creates a mess is its owner. A mess has exactly one owner, and the owner can transfer ownership to another member.
- A mess has exactly one manager. At first, the owner is the manager. The owner or the manager can hand the manager role to another member; the change takes effect on the 1st of the next month.
- A user can be a member of at most one mess, but a mess can have many members.

## Joining and leaving

- To join a mess, a user enters a random join key from the owner. Each key works only once.
- A member can't simply leave. They request to leave, and the owner must approve.
- Leaving takes effect at the end of the month. From the 1st of the next month the member is no longer counted. They are removed once that month is final and their balance is settled.
- The owner can't remove a member by force. When the owner starts a removal, all members are notified, and every member except the one being removed must agree. A removal also takes effect at the end of the month.
- A member can't leave or be removed while they owe money or are owed money.
- The owner can't delete the mess while it has other members. Every other member must leave first.

## Months

- A month is a calendar month: from the 1st to its last day (28, 29, 30 or 31).
- Months start automatically on the 1st and close automatically after their last day. Nobody starts or closes them.
- After a month closes, its meal counts can't change. For 24 hours after it closes (the grace period), members can still add groceries for it. After that, the month is final.
- Payments can still be recorded after a month closes.
- Members can look at past months' data and the activity log.

## Daily meal tracking (quick entry)

- The manager sets how many meal times each day has (0 to 4) and names them, e.g. Breakfast, Lunch, Dinner.
- For each meal time, a member enters how many meals they want. The default is 1: a member who doesn't change it is counted for one meal.
- Example: if a member sets 2 for Breakfast, their meal count for that breakfast is 2.
- The manager can lock and unlock a meal time. While it is locked, members can't change their meal counts, so counts can't be changed after the meal is over.
- Members can see their past daily meal records.

## Expenses

### Groceries

- Any member can add groceries they bought: what they bought, how much (kg, pounds, pieces, etc.) and the price.
- A member's grocery cost is the total of all groceries they bought in the month.

### Other costs

- The owner can set other costs, such as rent or utilities. There can be several, and the owner can edit them later.
- A cost can apply to everyone or to individual members.
- A cost can have a due date. A member who pays late must pay a fine.
- The app doesn't transfer money. Members live together and pay each other directly; the app only records the payments.

## Meal rate and bills

- **Meal rate** = total grocery cost of everyone in the month ÷ total meal count of everyone in the month.
- **Meal cost** of a member = meal rate × that member's total meal count in the month.
- **Balance** of a member = their total grocery cost in the month − their meal cost.
  - Below 0: the member owes that amount to the manager (due).
  - Above 0: the manager owes that amount to the member (owed).
  - Money changes hands in absolute amounts.
- Debts stay between the same people when the manager or owner changes. Example: `A` is the manager in May, and `B` owes `A` money for May. In June, `C` becomes the manager. `B` still owes `A`, not `C`.

## Payments

- When a member gives money to the owner, or the owner gives money to a member, the owner records it.
- The manager records payments between themselves and members in the same way.

## Reports and analytics

- Charts, monthly visualizations and analytics for data-driven decisions.
- Payment tracking, so members can follow their individual balances.

## Notifications and reminders

- A member is notified when the manager or owner records a payment involving them.
- Members get a reminder before a due date, so they aren't fined.

## Cloud backup and sync

- Data is stored in the cloud.
- Members can add groceries offline. The entries sync when the phone is back online.
- Meal counts can only be entered online. A meal count for a locked meal time is rejected.
- Edge case: a member adds a grocery offline, and the month closes and its 24-hour grace period ends before the entry syncs. The entry is rejected.
- Real-time sync: all members see updated meal and expense data immediately, on every device.
- Data is cached on the phone, so users can view records, and make some changes, without an internet connection.
- Changes made offline sync as soon as the connection is back.
- Different access levels protect data privacy and prevent unauthorized changes.
- Meal rates and dues are recalculated automatically as expenses and meal counts change.
