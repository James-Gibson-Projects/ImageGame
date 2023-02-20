package presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import model.messages.InvitationsState
import org.jetbrains.compose.web.ExperimentalComposeWebSvgApi
import org.jetbrains.compose.web.attributes.alt
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.svg.*
import presentation.view_models.FriendRequestsViewModel

@OptIn(ExperimentalComposeWebSvgApi::class)
@Composable
fun TopBar() {
    Nav(attrs = { classes("bg-gray-800".split(" ")); }) {
        Div(attrs = { classes("mx-auto max-w-7xl px-2 sm:px-6 lg:px-8".split(" ")); }) {
            Div(attrs = { classes("relative flex h-16 items-center justify-between".split(" ")); }) {
                Div(attrs = { classes("absolute inset-y-0 left-0 flex items-center sm:hidden".split(" ")); }) {
                    // Mobile menu button
                    MobileMenuButton()
                }

                Div(attrs = { classes("flex flex-1 items-center justify-center sm:items-stretch sm:justify-start".split(" ")) }) {
                    CompanyLogo()
                    ToolbarItems()
                }

                Div(attrs = {
                    classes("absolute inset-y-0 right-0 flex items-center pr-2 sm:static sm:inset-auto sm:ml-6 sm:pr-0".split(" "))
                }) {
                    Button(attrs = { classes("rounded-full bg-gray-800 p-1 text-gray-400 hover:text-white focus:outline-none focus:ring-2 focus:ring-white focus:ring-offset-2 focus:ring-offset-gray-800".split(" ")) }) {
                        Span(attrs = { classes("sr-only".split(" ")); }) {
                            Text("View notifications")
                        }
                        // Heroicon name: outline/bell
                        Svg(attrs = { classes("h-6 w-6".split(" ")); xmlns("http://www.w3.org/2000/svg"); fill("none"); viewBox("0 0 24 24") }) {
                            Path("M14.857 17.082a23.848 23.848 0 005.454-1.31A8.967 8.967 0 0118 9.75v-.7V9A6 6 0 006 9v.75a8.967 8.967 0 01-2.312 6.022c1.733.64 3.56 1.085 5.455 1.31m5.714 0a24.255 24.255 0 01-5.714 0m5.714 0a3 3 0 11-5.714 0")
                        }
                    }
                    // Profile dropdown
                    FriendRequestDropDown(FriendRequestsViewModel())
                }
            }
        }

        // Mobile menu, show/hide based on menu state.
        MobileMenu()
    }
}

@Composable
private fun MobileMenu() {
    Div(attrs = { classes("sm:hidden".split(" ")); id("mobile-menu"); }) {
        Div(attrs = { classes("space-y-1 px-2 pt-2 pb-3".split(" ")); }) {
            // Current: "bg-gray-900 text-white", Default: "text-gray-300 hover:bg-gray-700 hover:text-white"
            A(attrs = { classes("bg-gray-900 text-white block px-3 py-2 rounded-md text-base font-medium".split(" ")) }) {
                Text("Dashboard")
            }

            A(attrs = {
                classes(
                    "text-gray-300 hover:bg-gray-700 hover:text-white block px-3 py-2 rounded-md text-base font-medium".split(
                        " "
                    )
                )
            }) {
                Text("Team")
            }

            A(attrs = {
                classes(
                    "text-gray-300 hover:bg-gray-700 hover:text-white block px-3 py-2 rounded-md text-base font-medium".split(
                        " "
                    )
                )
            }) {
                Text("Projects")
            }

            A(attrs = {
                classes(
                    "text-gray-300 hover:bg-gray-700 hover:text-white block px-3 py-2 rounded-md text-base font-medium".split(
                        " "
                    )
                )
            }) {
                Text("Calendar")
            }
        }
    }
}
@Composable
private fun ToolbarItems() {
    Div(attrs = { classes("hidden sm:ml-6 sm:block".split(" ")); }) {
        Div(attrs = { classes("flex space-x-4".split(" ")); }) {
            // Current: "bg-gray-900 text-white", Default: "text-gray-300 hover:bg-gray-700 hover:text-white"
            A(attrs = { classes("bg-gray-900 text-white px-3 py-2 rounded-md text-sm font-medium".split(" ")); }) {
                Text("Dashboard")
            }

            A(attrs = { classes("text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium".split(" ")) }) {
                Text("Team")
            }

            A(attrs = { classes("text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium".split(" ")) }) {
                Text("Projects")
            }

            A(attrs = { classes("text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium".split(" ")) }) {
                Text("Calendar")
            }
        }
    }
}
@Composable
private fun CompanyLogo() {
    Div(attrs = { classes("flex flex-shrink-0 items-center".split(" ")); }) {
        Img(
            src = "https://tailwindui.com/img/logos/mark.svg?color=indigo&shade=500",
            attrs = { classes("block h-8 w-auto lg:hidden".split(" ")); alt("Your Company") }
        )
        Img(
            src = "https://tailwindui.com/img/logos/mark.svg?color=indigo&shade=500",
            attrs = { classes("hidden h-8 w-auto lg:block".split(" ")); alt("Your Company") }
        )
    }
}

@Composable
private fun ProfileDropdown() {
    Div(attrs = { classes("relative ml-3".split(" ")) }) {
        Div(attrs = {}) {
            Button(attrs = {
                classes("flex rounded-full bg-gray-800 text-sm focus:outline-none focus:ring-2 focus:ring-white focus:ring-offset-2 focus:ring-offset-gray-800".split(" ")); id("user-menu-button") }) {
                Span(attrs = { classes("sr-only".split(" ")) }) {
                    Text("Open user menu")
                }
                Img(
                    src = "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80",
                    attrs = { classes("h-8 w-8 rounded-full".split(" ")); alt(""); }
                )
            }
        }
        Div(attrs = { classes("absolute right-0 z-10 mt-2 w-48 origin-top-right rounded-md bg-white py-1 shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none".split(" ")) }) {
            // Active: "bg-gray-100", Not Active: ""
            A(attrs = { classes("block px-4 py-2 text-sm text-gray-700".split(" ")); id("user-menu-item-0"); }) {
                Text("Your Profile")
            }
            A(attrs = { classes("block px-4 py-2 text-sm text-gray-700".split(" ")); id("user-menu-item-1"); }) {
                Text("Settings")
            }
            A(attrs = { classes("block px-4 py-2 text-sm text-gray-700".split(" ")); id("user-menu-item-2"); }) {
                Text("Sign out")
            }
        }
    }
}
@OptIn(ExperimentalComposeWebSvgApi::class)
@Composable
private fun FriendRequestDropDown(viewModel: FriendRequestsViewModel) {
    val requests: InvitationsState? by viewModel
        .friendRequestsStateFlow
        .collectAsState(null)
    Div(attrs = { classes("relative ml-3".split(" ")) }) {
        Div(attrs = {}) {
            Button(attrs = {
                classes("flex rounded-full bg-gray-800 text-sm focus:outline-none focus:ring-2 focus:ring-white focus:ring-offset-2 focus:ring-offset-gray-800".split(" ")); id("user-menu-button") }) {
                Span(attrs = { classes("sr-only".split(" ")) }) {
                    Text("Open friend requests")
                }
                Svg {
                    Image("/static/svg/friend_request.svg")
                }
            }
        }
        Div(attrs = { classes("absolute right-0 z-10 mt-2 w-48 origin-top-right rounded-md bg-white py-1 shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none".split(" ")) }) {
            // Active: "bg-gray-100", Not Active: ""
            requests?.incoming?.forEachIndexed { index, username ->
                A(attrs = { classes("block px-4 py-2 text-sm text-gray-700".split(" ")); id("user-menu-item-$index"); }) {
                    Text(username)
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalComposeWebSvgApi::class)
private fun MobileMenuButton() {
    Button(attrs = { classes("inline-flex items-center justify-center rounded-md p-2 text-gray-400 hover:bg-gray-700 hover:text-white focus:outline-none focus:ring-2 focus:ring-inset focus:ring-white".split(" ")) }) {
        Span(attrs = { classes("sr-only".split(" ")); }) {
            Text("Open main menu")
        }
        Svg(attrs = { classes("block h-6 w-6".split(" ")); xmlns("http://www.w3.org/2000/svg"); fill("none"); viewBox("0 0 24 24") }) {
            Path("M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5")
        }
        Svg(attrs = {
            classes("hidden h-6 w-6".split(" ")); xmlns("http://www.w3.org/2000/svg"); fill("none"); viewBox(
            "0 0 24 24"
        )
        }) {
            Path("M6 18L18 6M6 6l12 12")
        }
    }
}

