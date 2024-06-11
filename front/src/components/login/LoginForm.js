import React from 'react';
import { useForm } from 'react-hook-form';
import { login } from '../../services/UserService';
import { TextField, Button, Container, Typography, Box } from '@mui/material';
import { Link, useNavigate } from "react-router-dom";

const LoginForm = () => {
  const { register, handleSubmit, formState: { errors } } = useForm();
  const navigate = useNavigate();

  const onSubmit = async (data) => {
    try {
      const response = await login(data);
      localStorage.setItem('accessToken', response.accessToken);
      console.log('Login successful:', response.accessToken);
      navigate("/home");
    } catch (error) {
      console.error('Login error:', error.message);
    }
  };

  return (
    <Container maxWidth="xs" sx={{ mt: 20 }}>
      <Box sx={{ mt: 8 }}>
        <Typography variant="h4" component="h1" gutterBottom textAlign="center">
          Login
        </Typography>
        <form onSubmit={handleSubmit(onSubmit)}>
          <Box sx={{ mb: 2 }}>
            <TextField
              fullWidth
              id="email"
              label="Email"
              type="email"
              {...register('email', {
                required: 'Email is required',
                pattern: {
                  value: /^[\w-\\.]+@([\w-]+\.)+[\w-]{2,4}$/,
                  message: 'Invalid email address'
                }
              })}
              error={!!errors.email}
              helperText={errors.email ? errors.email.message : ''}
            />
          </Box>
          <Box sx={{ mb: 2 }}>
            <TextField
              fullWidth
              id="password"
              label="Password"
              type="password"
              {...register('password', { required: 'Password is required' })}
              error={!!errors.password}
              helperText={errors.password ? errors.password.message : ''}
            />
          </Box>
          <Button type="submit" variant="contained" color="primary" fullWidth sx={{ mt: 3, mb: 2 }}>
            Login
          </Button>
        </form>
      </Box>
    </Container>
  );
};

export default LoginForm;
