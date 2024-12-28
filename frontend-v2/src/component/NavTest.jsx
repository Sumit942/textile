import React, { useState } from 'react';

const NavTest = ({ items }) => {
  return (
    <nav className="bg-gray-800">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          <div className="flex items-center">
            <div className="flex-shrink-0">
              <img className="h-8 w-8" src="vite.svg" alt="Workflow" />
            </div>
            <div className="hide md:block">
              <ul className="ml-10 flex items-baseline space-x-4">
                {items.map((item) => (
                  <NavbarItem key={item.id} item={item} />
                ))}
              </ul>
            </div>
          </div>
        </div>
      </div>
    </nav>
  );
};

const NavbarItem = ({ item }) => {
  const [isOpen, setIsOpen] = useState(false);

  const toggleMenu = () => {
    setIsOpen(!isOpen);
  };

  return (
    <li className="relative group">
      <a
        href={item.url}
        className="text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium"
        onMouseEnter={toggleMenu}
        onMouseLeave={toggleMenu}
      >
        {item.name}
      </a>
      {item.children && item.children.length > 0 && (
        <ul className={`absolute left-0 mt-2 w-48 bg-gray-800 rounded-md shadow-lg ${isOpen ? 'block' : 'hidden'}`}>
          {item.children.map((child) => (
            <li key={child.id} className="text-gray-300 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm">
              <a href={child.url}>{child.name}</a>
            </li>
          ))}
        </ul>
      )}
    </li>
  );
};

export default NavTest;