import { useState } from "react";
import NavbarItem from "./NavbarItem";
import { NavbarItemMobile } from "./NavItemMobile";

const Navbar = ({ items }) => {
  console.log('navbar')
  const [isOpen, setIsOpen] = useState(false);

  const toggleMenu = () => {
    setIsOpen(!isOpen);
  };

  return (
    <header className="relative z-40">
    <nav className="border-gray-200 bg-gray-800">
      <div className="flex flex-wrap justify-between items-center mx-auto max-w-sreen-xl p-4">
        <div className="flex space-x-3">
          <img className="h-8 w-8" src="/vite.svg" alt="Workflow" />
          <span className="self-center text-2xl font-semibold whitespace-nowrap text-white">
            <span>Shreeram Textile Ind<span className="md:hidden">.</span><span className="sd:hidden">ustries</span></span>
          </span>
        </div>
        <div className="-mr-2 flex md:hidden">
            <button
              type="button"
              className="bg-gray-700 inline-flex items-center justify-center p-2 rounded-md text-gray-400 hover:text-white hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-gray-800 focus:ring-white"
              aria-controls="mobile-menu"
              aria-expanded="false"
              onClick={toggleMenu}
            >
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
    </nav>
    <nav className="bg-gray-700">
      <div className="sd:hidden max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          <div className="flex items-center">
            <div className="sd:hidden md:block">
              <ul className="ml-10 flex items-baseline space-x-4">
                {
                  items.map((item) => (
                    <NavbarItem key={item.id} item={item} />
                  ))
                }
              </ul>
            </div>
          </div>
        </div>
      </div>
      <div className={`md:hidden ${isOpen ? "block" : "hidden"}`}>
        <ul className="px-2 pt-2 pb-3 space-y-1">
          {
            items.map((item) => (
              <NavbarItemMobile key={item.id} item={item} toggleMenu={toggleMenu} />
            ))
          }
        </ul>
      </div>
      
    </nav>
    </header>
  );
};

export default Navbar;
