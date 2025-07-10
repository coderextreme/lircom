# Change Division into division done by hand.
    * Fix stopping early

# Fix Sort Top/Sort Bottom
    * Nearly works
    * Try different sizes

# Fix Safari/iOS clipboard/loading
    * Disabled clipboardMode (NO SOLUTION YET)
    * Loading works on https://coderextreme.github.io/lircom/

# Get Jed a couple of machines (COMPLETE)
    * Division example
    * Sort example

# It's a 4x4 multiply, not add
    * Verify top row: "Don't Know", "Right Turn", ... (Verified)
    * Possibly erase text label
    * Figure out how to format input and ouput
    * Currently, if you type in 0's you get 0's out, and if you type 1's, you get 1's out.  Where are the 0's?  (FIXED)

# Implemented Jed's loading
    * Jed to provide data file
    * Add generate/parse+load options for data files
    * John to implement loading how Jed wants.
    * Devise a way to generate a loading file from a current machine
    * Numbers for personalities (bit string?) (BELAYED)


# allow for quick choosing of personalities
    * Press for personality (1, 2, 3, ...) (COMPLETE)
    * Array or enum for personalities (COMPLETE)
    * Provide context menu, possibly with number, text, and icon (BELAYED)
    * Add icon to Create menu, if not to big on phones. (BELAYED)
    * Allow to freeze/unfreeze buffer personality changes for bit entry (USE INKPOT)
    * Unfreeze then context menu to blank a buffer (accelerated?) (BELAYED)

# Implement a high-level language
    * JSON, XML or custom

# Create a way or document a way to create "cookie cutter" machines that can be pasted for creating sorting machines.
    * Partially implemented, look at impactVL.Module

# Compose machines usign Impact.java

# Route Graphs import/export via the Menu.
    * Not tables
    * Use Route Graph format

# Bring up keyboard/Ctrl/Cmd in CheerpJ on mobile (IMPOSSIBLE?)


# Better ideas for Buffer input/output
    * Inkpot for 0, 1, Backspace, Replace
    * Save output buffers in export format
    * read input buffers in import format (VERIFY)
    * Select buffers to save

# I/O languages
    * table
        . Add buffers
    * Route graph
    * Jed's external personality loading format
    * Floweth, high level
    * Provide textarea to cut/copy/paste route graph
