import React, { useState } from "react";
import { Link } from "react-router-dom";

// Recursive Component
export const NavbarItemMobile = ({ liClassName, item, toggleMenu }) => {
  console.log('navitemmobile')
  const [isOpen, setIsOpen] = useState(false);

  const handleSubMenuToggle = () => {
    setIsOpen(!isOpen)
    console.log('ahahaha')
  };
 
  return (
    <>
      <li className="md:hidden">
        <div className="text-gray-300 hover:bg-gray-700 hover:text-white block px-3 py-2 rounded-md text-base font-medium flex">
          {item.url ? (
            <Link
              to={item.url}
              className="text-gray-300 hover:bg-gray-700 hover:text-white"
              onClick={() => toggleMenu(false)}
            >
              {item.name}
            </Link>
          ) : (
            <span className="text-gray-300 hover:bg-gray-700 hover:text-white">
              {item.name}
            </span>
          )}
          <button onClick={handleSubMenuToggle}>
            {item.children && item.children.length > 0 && (
              <svg
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
                strokeWidth={1.5}
                stroke="currentColor"
                className="w-4 h-4 ml-5"
              >
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  d="M19.5 8.25l-7.5 7.5-7.5-7.5"
                />
              </svg>
            )}
          </button>
        </div>
        {item.children && item.children.length > 0 && (
          <ul
            className={`ml-4 
            ${isOpen ? "show" : "hide"}
          `}
          >
            {item.children.map((child) => (
              <NavbarItemMobile key={child.id} item={child} toggleMenu={toggleMenu}/>
            ))}
          </ul>
        )}
      </li>
    </>
  );
};
