# cscodetest

This is a take home assessment for a non-descript company for a Senior Android Developer position. After concluding the interview process this repo will be archived.

## Installation

Clone via SSH:

`git clone git@github.com:bmalumphy/cscodetest.git`

...or pull via Android Studio's built in Source Control Manager.

Gradle enviroments should be built as a JDK 1.8/Gradle 7.0.4 versions. If you have uninstalled JDK 1.8 due to Gradle 7.1/7.2's JDK 11 requirements causing some gradle errors,
you may have to reinstall 1.8.

Emulation has been tested on Pixel 3.

Devices have been tested on a Pixel 5 and Lenovo 10 inch tablet.

## Product Description

The given REST API exists at `https://cscodetest.herokuapp.com/api/`. It is meant to act as a backend for a product management/inventory system.

Authentication is provided through [Basic Auth](https://en.wikipedia.org/wiki/Basic_access_authentication) on `/status` endpoint. Once a response is received, the `JWT` will be
cached in an encrypted `SharedPreferences` instance on device via `AuthenticationDataManager`.

Revokation and refresh tokens are not implemented on the API, so `JWTs` last forever and revokation happens automatically when a new `JWT` is created. Revokation on device 
merely deletes the current `JWT` from `SharedPreferences`.

Once in the dashboard, current implementations have the following series of operations available:

1. Pull down and view Products listed in User's `/products` endpoint.
    a. These results are paginated in clumps of 20, and will paginate automatically once the `RecyclerView` gets within 5 products of the last Product currently in the List.
2. Logout to return to the `LoginFragment`.
3. Add a Mock item to the `/product` endpoint.
4. Delete an item in the product's list via an `AlertDialog` and deleting via the `/product` endpoint.

## API Issues

Currently the API given for the assessment has some flaws (probably purposeful). `POST` to the `/product` endpoint will result in nameless products with no price, independent
of the body for the request. In addition, there seems to be little in the way of proper 400-level responses delivered from the API. Specifically, it will not kick back a request with extraneous 
keys in the JSON request. It is noted in the PDF description that the server may throw 500 responses on occasion, but this hadn't been viewed/confirmed nor is it clear if this is likely to occur 
from one endpoint more than another. Currently waiting on clarification on this from the Mobile Lead.

## TODO

[ ] Evaluate Creation function given API limitations, and upon fixes on the backend implementing a Fragment to allow for manual Product creations
[ ] Basic Unit Tests around ViewModels/Managers
[ ] Product Details Fragment

## Tech Debt

For the purposes of keeping the app somewhat expedient in its submission, a few liberties were taken:

1. Modularity is flimsy and there are many services that should be pulled into their own internal libraries/modules for encapsulation. Ideally, for all management services, it would be
nice to only have consumers care about the Managers themselves rather than some of the tooling under them (Like `AuthenticationDataManager` in the case of `AuthenticationManager`).

2. Navigation has been kept very simple to avoid building a full service around it (Fragment Factories, etc.). Efforts will be made to ensure that no cyclic references will persist in the app
before submission. Ideally this would be handled in an elegant fashion to allow for a more expansive application that would entail Inventory management/Search/etc.

3. Testing will be reserved for ViewModels/Managers, and not things like `Hilt` modules, Fragments, or data classes.
