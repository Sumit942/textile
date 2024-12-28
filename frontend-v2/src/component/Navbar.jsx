import { useState } from "react";
import NavbarItem from "./NavbarItem";

const Navbar = ({ items }) => {
  const [isOpen, setIsOpen] = useState(false);

  const toggleMenu = () => {
    setIsOpen(!isOpen);
  };

  return (
    <nav className="bg-gray-800">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <img className="h-8 w-8" src="vite.svg" alt="Workflow" />
            </div>
            <div className="sd:hidden md:block">
              <ul className="ml-10 flex items-baseline space-x-4 show">
                {
                  items.map((item) => (
                    <NavbarItem key={item.id} item={item} />
                  ))
                }
              </ul>
            </div>
          </div>
          <div className="-mr-2 flex md:hidden">
            <button
              type="button"
              className="bg-gray-800 inline-flex items-center justify-center p-2 rounded-md text-gray-400 hover:text-white hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-gray-800 focus:ring-white"
              aria-controls="mobile-menu"
              aria-expanded="false"
              onClick={toggleMenu}
            >
              <span className="sr-only">Open main menu</span>
              {/*
                Heroicons: menu
                Menu open: "hidden", Menu closed: "block"
              */}
              <svg
                className={`h-6 w-6 ${isOpen ? "hidden" : "block"}`}
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                strokeWidth="1.5"
                stroke="currentColor"
                aria-hidden="true"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5"
                />
              </svg>
              {/*
                Heroicons: x-mark
                Menu open: "block", Menu closed: "hidden"
              */}
              <svg
                className={`h-6 w-6 ${isOpen ? "block" : "hidden"}`}
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                strokeWidth="1.5"
                stroke="currentColor"
                aria-hidden="true"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M6 18L18 6"
                />
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M6 6l12 12"
                />
              </svg>
            </button>
          </div>
        </div>
        {/* Mobile menu, show/hide based on menu state. */}
        <div className={`md:hidden ${isOpen ? "block" : "hidden"}`}>
          <ul className="px-2 pt-2 pb-3 space-y-1">
            {/* Add more mobile navigation links here */}
            {
              items.map((item) => (
                <NavbarItem key={item.id} item={item} />
              ))
            }
          </ul>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
