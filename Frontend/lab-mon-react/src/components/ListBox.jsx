import React from 'react';
import Box from '@mui/material/Box';
import Grid from '@mui/material/Grid';
import Typography from '@mui/material/Typography';
import List from '@mui/material/List';
import ListItem from '@mui/material/ListItem';
import ListItemText from '@mui/material/ListItemText';

const ListBox = ({ data, style }) => {
  const generateListItems = () => {
    return data.map((item, index) => (
      <ListItem key={index} dense>
        <ListItemText primary={item} />
      </ListItem>
    ));
  };

  return (
    <Box sx={{ flexGrow: 1, maxWidth: 752 }}>
      <Grid container spacing={2}>
        <Grid item xs={12} md={6}>
          <Typography sx={{ mt: 4, mb: 2 }} variant="h6" component="div">
          </Typography>
          <List dense={true} sx={style}>
            {generateListItems()}
          </List>
        </Grid>
      </Grid>
    </Box>
  );
};

export default ListBox;