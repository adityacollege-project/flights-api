# Changelog

All notable changes to this project will be documented in this file.

## [Unreleased]

### Added
- Added flights search API
- validated the flight search request
- Integrated with Mondee supplier API
- Mapping the Supplier API response
- Persisting the Search request in Mongo
- Enabling the Cache for search End point
- Integrated the Branded fares in the Search
- update to test github to bugzilla integration

### Changed

### Deprecated

### Removed

### Fixed
- 2024-01-10 : Addressed onward date of first object to be before onward date of second object in the case of multi-city
- 2024-01-10 : Addressed from-to fields case-sensitivity issue  
- 2024-02-08 : Fixed all book api persistance issues.
- 2024-02-08 : match orderid & supplier pnr in ardor db for post book operations(Ticket, cancel, read pnr).
- 2024-02-12 : Update transaction status on cancel, orderticket services.
- 2024-02-20 : Bug 121 - Fixed air_trans_dtl persistance issue.
- 2024-02-20 : Bug 125 - Fixed DateOfBirth Validation issue.
- 2024-02-20 : Bug 154 - Addressed air_pax_bkg_info peristance comments.
- 2024-02-21 : Bug 124 - Addressed Failed bookings persisting in air_trans_hdr table as booked
- 2024-02-21 : Bug 157 - Addressed More than 5 flights getting flight details.
- 2024-02-21 : air_pax_bkg_info - Addressed orgdest column values are in lower case.
- 2024-02-21 : air_pax_bkg_info - Addressed the trans_status not updating as CAN after Cancellation

- 2024-02-22 : Bug 160 Fixed air_pax_bkg_info persistance
- 2024-02-22 : Bug 162 Fixed air_pax_bkg_info persistance
- 2024-02-22 : Bug 163 Fixed air_pax_bkg_info persistance
- 2024-02-22 : Bug 164 Fixed air_pax_bkg_info persistance
- 2024-02-22 : Bug 165 Fixed air_pax_bkg_info persistance
- 2024-02-22 : Bug 166 Fixed air_pax_bkg_info persistance

- 2024-02-23 : Bug 144 Fixed Internal server error for the Invalid Locations
- 2024-02-23 : Bug 161 Fixed air_info fare break up showing full fares
- 2024-02-23 : Bug 165 Fixed air_pax_bkg_info trans_status showing as BKG after Cancel
- 2024-02-23 : Bug 166 Fixed air_pax_bkg_info persistance

- 2024-02-24 : Bug 191 Removed Header Logs For UserAPI
- 2024-02-24 : Bug 161 air_info fareBreakup addressed
- 2024-02-24 : Bug 165 air_pax_bkg_info cancel status updated
- 
- 2024-02-27 : 183,184,197,166,174,175,176,200

### Security

